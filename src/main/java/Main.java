import Dto.ProgramBlocksDto;
import Entities.Program;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public String readFileAsString(String fileName)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static String text;

    static ProgramLinesParser programLinesParser;
    static ProgramEntitiesParser programEntitiesParser;
    static Interpreter interpreter;

    public static void main(String[] args) {
        Main main = new Main();
        try {
            text = main.readFileAsString("src/main/resources/program");
        } catch (Exception e) {
            e.printStackTrace();
        }
        programLinesParser = new ProgramLinesParser();
        programEntitiesParser = new ProgramEntitiesParser();
        ProgramBlocksDto result = programLinesParser.parse(text);
        Program executionResult = programEntitiesParser.parse(result);
        System.out.println(executionResult);
        interpreter = new Interpreter();
        interpreter.execute(executionResult);
    }
}