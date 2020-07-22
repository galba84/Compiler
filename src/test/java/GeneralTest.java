import Dto.ProgramBlocksDto;
import Entities.Program;
import Entities.Variable;
import Enums.VariableType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GeneralTest {

    ProgramLinesParser programLinesParser;
    ProgramEntitiesParser programEntitiesParser;
    Interpreter interpreter;

    @BeforeEach
    void setUp() {
        programLinesParser = new ProgramLinesParser();
        programEntitiesParser = new ProgramEntitiesParser();

    }

    @Test
    public void test() {
        ProgramBlocksDto result = programLinesParser.parse(ProgramLinesParserTest.text);
        Program restResult = programEntitiesParser.parse(result);
        Program expectedResult = getExpectedResult();
        System.out.println(restResult);

        assertEquals(expectedResult.variablesContainer,restResult.variablesContainer);
//        interpreter = new Interpreter();
//        interpreter.execute(restResult);
    }

    private Program getExpectedResult() {
        Program program = new Program();
        try {
            program.variablesContainer.add(new Variable("i", "null", VariableType.integer));
            program.variablesContainer.add(new Variable("bb", "null", VariableType.bool));
            program.variablesContainer.add(new Variable("r", "null", VariableType.real));

        }catch (Exception e){}
        return program;
    }

}

