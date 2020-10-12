import Entities.*;
import Exceptions.ExpressionException;

import java.util.*;

public class ExpressionParser {
    public Expression expression;

    public Expression parseCondition(String expressionString) throws ExpressionException {
        String replace = expressionString.replace("WHILE", "").replace("(", "").replace(")", "").trim();
        expression = new Expression();
        expression.line = expressionString;
        List<ExpressionOperationPartType> expressionPartsFromExpressionWithOpenBrackets = getExpressionPartsFromExpressionWithOpenBrackets(replace);
        expression.orderedExpressionOperations.add(new ExpressionOperation(
                (Operand) expressionPartsFromExpressionWithOpenBrackets.get(0),
                (Operand) expressionPartsFromExpressionWithOpenBrackets.get(2),
                (Operator) expressionPartsFromExpressionWithOpenBrackets.get(1)));
        if (expressionPartsFromExpressionWithOpenBrackets.size() > 3) {
            throw new ExpressionException("ERROR: condition should have one operator " + replace);
        }
        return expression;
    }

    public Expression parse(String expressionString)  {
        String s = expressionString.replace(";", "").replaceAll(" ","");
        expression = new Expression();
        expression.line = s;
        BracketsOpener bracketsOpener = new BracketsOpener();
        bracketsOpener.openBrackets(s);
        LinkedList<String> linkedList = new LinkedList<>();
        int ii=0;
        for (String ss:bracketsOpener.tempBuffer.values()
             ) {
            linkedList.add(bracketsOpener.tempBuffer.get("$"+ii));
            ii++;
        }
        for (Integer i = bracketsOpener.tempBuffer.size()-1; i>= 0 ; i--) {
            LinkedHashMap<String, String> stringStringLinkedHashMap = new LinkedHashMap<>();
            stringStringLinkedHashMap.put("$"+i.toString(),linkedList.get(i));
            expression.expressionPartsPrecedenceLeveledMap.put("$"+i.toString(),stringStringLinkedHashMap);
        }

        for (Map<String, String> map :
                this.expression.expressionPartsPrecedenceLeveledMap.values()) {
            List<String> values = new ArrayList<>(map.values());
            Collections.reverse(values);
            List<String> keys = new ArrayList<>(map.keySet());
            Collections.reverse(keys);
        }
        convertExpressionPrecedenceLevelsIntoOrderedExpressionOperations();

        return expression;
    }

    private void convertExpressionPrecedenceLevelsIntoOrderedExpressionOperations() {
        Collection<Map<String, String>> values = expression.expressionPartsPrecedenceLeveledMap.values();
        Iterator<Map<String, String>> iterator = values.iterator();
        for (int i = 0; i < values.size(); i++) {
            Map<String, String> next = iterator.next();
            for (String s : next.values()
            ) {
                List<ExpressionOperationPartType> expressionPartsFromExpressionWithOpenBrackets = getExpressionPartsFromExpressionWithOpenBrackets(s);
                Map<String, ExpressionOperation> orderedExpressionOperationsOfLevel = expression.convertExpressionPartsIntoOrderedExpressionOperations(expressionPartsFromExpressionWithOpenBrackets);
                expression.orderedExpressionOperations.addAll(orderedExpressionOperationsOfLevel.values());
            }
        }
    }

    public List<ExpressionOperationPartType> getExpressionPartsFromExpressionWithOpenBrackets(String expressionString) {
        String buffer = "";
        List<ExpressionOperationPartType> result = new LinkedList<>();
        for (int i = 0; i < expressionString.length(); i++) {
            String subStr = expressionString.substring(i, i + 1);
            if (subStr.equals(":")) {
                if (expressionString.length() > i + 1) {
                    String subStrNext = expressionString.substring(i + 1, i + 2);
                    if (subStrNext.equals("=")) {
                        subStr += subStrNext;
                        i++;
                    }
                }
            }
            if (isOperator(subStr)) {
                if (buffer.length() > 0) {
                    result.add(new Operand(buffer));
                    buffer = "";
                }
                result.add(new Operator(subStr));
                continue;
            } else {
                buffer += subStr;
            }
            if (i == expressionString.length() - 1) {
                result.add(new Operand(buffer));
            }
        }
        return result;
    }

    public boolean isOperator(String s) {
        switch (s) {
            case "+":
                return true;
            case "-":
                return true;
            case "*":
                return true;
            case "/":
                return true;
            case "^":
                return true;
            case ":=":
                return true;
            case ">":
                return true;
            case "<":
                return true;
            case "=":
                return true;
        }
        return false;
    }
}

