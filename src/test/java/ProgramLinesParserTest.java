import Dto.ProgramBlocksDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramLinesParserTest {
    public static String text = "PROGRAM\n" +
            "VAR\n" +
            "integer i;\n" +
            "bool bb;\n" +
            "real r;\n" +
            "BEGIN\n" +
            "i:=22+3*4-14/1;\n" +
            "i:=i+22;\n" +
            "WHILE (i<100)\n" +
            "{\n" +
            "i:=i-3;\n" +
            "i:=i+22;\n" +

            "WHILE (i<10)\n" +
            "i:=i+2;\n" +
            "i:=i/2;\n" +
            "i:=i/4;\n" +
            "i:=i+21;\n" +
            "}\n" +
            "}\n" +
            "WHILE (i<277)\n" +
            "{\n" +
            "i:=i-5;\n" +
            "i:=i+33;\n" +
            "}\n" +
            "END";

    @Test
    public void test1() {
        ProgramLinesParser programLinesParser = new ProgramLinesParser();
        ProgramBlocksDto result = programLinesParser.parse(text);
        ProgramBlocksDto programBlocksDto = getProgramBlocksDto();
        System.out.println(result);
        assertEquals(result, programBlocksDto);
    }

    private ProgramBlocksDto getProgramBlocksDto() {
        ProgramBlocksDto programBlocksDto = new ProgramBlocksDto();
        programBlocksDto.variablesBlock.addAll(Arrays.asList("integer i;", "bool bb;", "real r;"));
        programBlocksDto.bodyBlock.addAll(Arrays.asList("i:=22+3*4-14/1;","i:=i+22;","WHILE (i<100)", "{", "i:=i-3;", "i:=i+22;", "WHILE (i<10)", "i:=i+2;",  "i:=i/2;","i:=i/4;", "i:=i+21;","}", "}", "WHILE (i<277)", "{","i:=i-5;", "i:=i+33;","}"));

        return programBlocksDto;
    }

}