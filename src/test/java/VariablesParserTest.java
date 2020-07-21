//import Entities.Variable;
//import Enums.VariableType;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
///*
//line should consist of 2 or 3 parts split by whitespace;
//First part should be VarTypeName
//Second part should be "id" or id together with semicolon "id;"
// */
//class VariablesParserTest {
//
//    @Test
//    void testParseHappy() {
//        String line = "INTEGER id;";
//        VariablesParser variablesParser = new VariablesParser();
//        try {
//            Variable variable = variablesParser.parse(line);
//            assertEquals(variable.name, "id");
//            assertEquals(variable.value, "");
//            assertEquals(variable.type, VariableType.integer);
//
//        } catch (Exception e) {
////            assertTrue(e.getMessage().equals("parse line failed"));
//        }
//    }
//
//    @Test
//    void testValidateLineHappy() {
//        String line = "INTEGER id;";
//        VariablesParser variablesParser = new VariablesParser();
//        try {
//            variablesParser.validateLine.validate(line);
//        } catch (Exception e) {
////            assertTrue(e.getMessage().equals("Line starts not with VarType, line should starts with VarType followed by var name enclosed by semicolon"));
//        }
//    }
//
//    @Test
//    void testValidateLineSingleParamUnhappy() {
//        String line = "INTEGERid;";
//        VariablesParser variablesParser = new VariablesParser();
//        try {
//            variablesParser.validateLine.validate(line);
//        } catch (Exception e) {
////            assertTrue(e.getMessage().equals("line should starts with VarType followed by var name enclosed by semicolon"));
//        }
//    }
//
//    @Test
//    void testValidateLineEndsWithSemicolonUnhappy() {
//        String line = "Integer id;";
//        VariablesParser variablesParser = new VariablesParser();
//        try {
//            variablesParser.validateLine.validate(line);
//        } catch (Exception e) {
//            assertTrue(e.getMessage().equals("Line starts not with VarType, line should starts with VarType followed by var name enclosed by semicolon"));
//        }
//    }
//
//    @Test
//    void testValidateLineStartsWithVarTypeNameUnhappy() {
//        String varLine = "INTEGE_ id";
//        VariablesParser variablesParser = new VariablesParser();
//        try {
//            variablesParser.validateLine.validate(varLine);
//        } catch (Exception e) {
//            assertTrue(e.getMessage().equals("Line starts not with VarType, line should starts with VarType followed by var name enclosed by semicolon"));
//        }
//    }
//
//    @Test
//    void testValidateVarNameUnhappy() {
//        String varLine = "INTEGER id";
//        VariablesParser variablesParser = new VariablesParser();
//        try {
//            variablesParser.validateLine.validate(varLine);
//        } catch (Exception e) {
//            assertTrue(e.getMessage().equals("line should be enclosed with semicolon"));
//        }
//    }
//
//}