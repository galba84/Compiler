import Entities.*;
import Exceptions.ExpressionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ExpressionParserTest {

    @BeforeEach
    void setUp() {
    }

//    @Test
//    public void testSortPrecedenceOrderSuccess() {
//        String string = "2+3*4-1=7:=90>4/1";
//        ExpressionParser expressionParser = new ExpressionParser();
//        Expression expressionObject = expressionParser.parse(string);
//
//        List<Operator> operators = expressionObject.getOperators(expressionParser.getExpressionPartsFromExpressionWithOpenBrackets(string));
//
//        Collections.sort(operators);
//        String operatorsString = operators.toString()
//                .replaceAll(",", "").replaceAll(" ", "")
//                .replaceAll("\\[", "").replaceAll("\\]", "");
//        assertEquals("*/+-=>:=", operatorsString);
//    }


    @Test
    public void testOpenBrackets() throws ExpressionException {
        String string =  "i:=22+(14-3*4)/1;" ;
        ExpressionParser expressionParser = new ExpressionParser();
        expressionParser.parse(string);
//        expressionParser.openBrackets(string);
        assertEquals("{2={2$1=11-11*12}, 1={1$4=111-111*121, 1$6=6-6, 1$2=9+9+d*10+2$1}, 0={0$5=91+91+d*101+1$4, 0$7=5+5+1$6-7, 0$3=4-4*1$2}, -1={-1$8=2+3+0$3*0$5*0$7-8}}", expressionParser.expression.expressionPartsPrecedenceLeveledMap.toString());
    }
//
//    @Test
//    public void testNestedPrimitivesMap() {
//        String string = "2+3+(4-4*(9+9+d*10+(11-11*12)))*(91+91+d*101+(111-111*121))*(5+5+(6-6)-7)-8";
//        ExpressionParser expressionParser = new ExpressionParser();
//        Expression expressionObject = expressionParser.parse(string);
//        List<List<String>> expected = new LinkedList<>();
//        expected.add(new LinkedList<String>(Arrays.asList("11-11*12")));
//        expected.add(new LinkedList<String>(Arrays.asList("111-111*121", "6-6", "9+9+d*10+2$1")));
//        expected.add(new LinkedList<String>(Arrays.asList("91+91+d*101+1$4, 5+5+1$6-7, 4-4*1$2")));
//        expected.add(new LinkedList<String>(Arrays.asList("2+3+0$3*0$5*0$7-8")));
//
//        Collection<Map<String, String>> values = expressionObject.expressionPartsPrecedenceLeveledMap.values();
//        Iterator<Map<String, String>> iterator = values.iterator();
//        for (int i = 0; i < values.size(); i++) {
//            Map<String, String> next = iterator.next();
//            assertEquals(expected.get(i).toString(), next.values().toString());
//        }
//
//    }

    @Test
    void testConvertExpressionPartIntoOrderedExpressionOperations() {
        String string = "2+3+(4-4*(9+9+d*10+(11-11*12)))*(91+91+d*101+(111-111*121))*(5+5+(6-6)-7)-8";
        ExpressionParser expressionParser = new ExpressionParser();
//        Expression expressionObject = expressionParser.parse(string);
//        Collection<Map<String, String>> values = expressionObject.expressionPartsPrecedenceLeveledMap.values();
//        Iterator<Map<String, String>> iterator = values.iterator();
//
//        System.out.println("result");
//        assertEquals(expressionObject.orderedExpressionOperations.get(0), new ExpressionOperation(new Operand("11"), new Operand("12"), new Operator("*")));
//        assertNotEquals(expressionObject.orderedExpressionOperations.get(0), new ExpressionOperation(new Operand("111"), new Operand("12"), new Operator("*")));
//        assertNotEquals(expressionObject.orderedExpressionOperations.get(0), new ExpressionOperation(new Operand("11"), new Operand("12"), new Operator("-")));
//        assertEquals(expressionObject.orderedExpressionOperations.get(expressionObject.orderedExpressionOperations.size()-1), new ExpressionOperation(new Operand("$99"), new Operand("8"), new Operator("-")));
    }

//    @Test
//    void ExpressionPartsFromExpressionWithOpenBrackets() {
//        String string = "2+3+4-4*111-111*$d3*5+5+6-6-7-8";
//        ExpressionParser expressionParser = new ExpressionParser();
//        List<ExpressionOperationPartType> expressionPartsFromExpressionWithOpenBrackets = expressionParser.getExpressionPartsFromExpressionWithOpenBrackets(string);
//        String expected = new String("[2, +, 3, +, 4, -, 4, *, 111, -, 111, *, $d3, *, 5, +, 5, +, 6, -, 6, -, 7, -, 8]");
//        assertEquals(expected, expressionPartsFromExpressionWithOpenBrackets.toString());
//    }
}