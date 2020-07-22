import Entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CycleExpressionTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testSortPrecedenceOrderSuccess() {
        String string =    "WHILE (i>0)\n" +
                "{\n" +
                "i:=i-3;\n" +
                "i:=i+2;\n" +
                "WHILE (i=10)\n" +
                "{\n" +
                "i:=i-6;\n" +
                "i:=i+7;\n" +
                "}\n" +
                "}\n" ;
        ExpressionParser expressionParser = new ExpressionParser();
//        Expression expressionObject = expressionParser.parse(string);

//        List<Operator> operators = expressionObject.getOperators(expressionParser.getExpressionPartsFromExpressionWithOpenBrackets(string));
//
//        Collections.sort(operators);
//        String operatorsString = operators.toString()
//                .replaceAll(",", "").replaceAll(" ", "")
//                .replaceAll("\\[", "").replaceAll("\\]", "");
//        assertEquals("*/+-=>:=", operatorsString);
    }



}