import Dto.ProgramBlocksDto;
import Entities.Program;
import Entities.Variable;
import Enums.VariableType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GeneralTest {

    ProgramBlocksParser programBlocksParser;
    ProgramParser programParser;
    Interpreter interpreter;

    @BeforeEach
    void setUp() {
        programBlocksParser = new ProgramBlocksParser();
        programParser = new ProgramParser();

    }

    @Test
    public void test() {
        ProgramBlocksDto result = programBlocksParser.parse(ProgramBlocksParserTest.text);
        Program restResult = programParser.parse(result);
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

