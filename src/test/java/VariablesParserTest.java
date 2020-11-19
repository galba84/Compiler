import Entities.Variable;
import Enums.VariableType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
line should consist of 2 or 3 parts split by whitespace;
First part should be VarTypeName
Second part should be "id" or id together with semicolon "id;"
 */
class VariablesParserTest {
    private VariablesParser testInstance = new VariablesParser();

    @Test
    public void shouldReturnVariable() throws Exception {
        Variable result = testInstance.parse("integer c;");
        assertEquals(VariableType.integer, result.type);
        assertEquals("", result.value);
        assertEquals("c", result.name);
    }

    @Test
    public void shouldReturnLineWithoutSemicolonAtTheEnd() {
        String result = testInstance.normalizeLine("Integer c;");
        assertEquals("Integer c", result);
    }

    @Test
    public void shouldReturnLineWithoutSemicolonAtTheEndSecondCase() {
        String result = testInstance.normalizeLine("Integer c ;");
        assertEquals("Integer c", result);
    }

}