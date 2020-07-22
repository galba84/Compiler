import Entities.*;
import Exceptions.ExpressionException;

import java.util.*;

public class ExpressionParser {
    private Integer level;
    public Expression expression;

    public Expression parseCondition(String expressionString) throws ExpressionException {
        String replace = expressionString.replace("WHILE", "").replace("(", "").replace(")", "").trim();
        expression = new Expression();
        expression.line = expressionString;
        level = 0;
        List<ExpressionOperationPartType> expressionPartsFromExpressionWithOpenBrackets = getExpressionPartsFromExpressionWithOpenBrackets(replace);
        expression.orderedExpressionOperations.add(new ExpressionOperation(
                (Operand) expressionPartsFromExpressionWithOpenBrackets.get(0),
                (Operand) expressionPartsFromExpressionWithOpenBrackets.get(2),
                (Operator) expressionPartsFromExpressionWithOpenBrackets.get(1)));
        if (expressionPartsFromExpressionWithOpenBrackets.size() > 3) {
            throw new ExpressionException("ERROR: condition shuold have one operator " + replace);
        }
        return expression;
    }

    public Expression parse(String expressionString)  {
        String s = expressionString.replace(";", "").replaceAll(" ","");
        expression = new Expression();
        expression.line = s;
        level = 0;
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



    public void openBrackets(String expressionString) {
        List<String> tempBuffer = new LinkedList<>();
        List<String> expressionSymbols = new LinkedList<>(Arrays.asList(expressionString.split("")));
//        int idI=0;
        String buffer = "";
        for (int i = 0; i < expressionSymbols.size(); i++) {
            String s = expressionSymbols.get(i);
            if (s.equals("(")) {
                level++;
                if (buffer.length() > 0) {
                    tempBuffer.add(buffer);
                    buffer = "";
                }
            } else if (s.equals(")")) {
                level--;
                if (buffer.length() > 0) {
                    //$
                    String id = expression.getOperationId();
//                    idI++;
                    putToMap(id, buffer);
                    if (tempBuffer.size() > 0) {
                        buffer = tempBuffer.remove(tempBuffer.size() - 1) + id;
                    }
                }
            } else {
                buffer += s;
            }
            if (i == expressionSymbols.size() - 1) {
                level--;
                String id = expression.getOperationId();
                putToMap(id, buffer);
//                idI++;
            }
        }
    }






    private void putToMap(String id, String buffer) {
        if (expression.expressionPartsPrecedenceLeveledMap.containsKey(level.toString())) {
            Map<String, String> stringStringMap = expression.expressionPartsPrecedenceLeveledMap.get(level.toString());
            stringStringMap.put(id, buffer);
        } else {
            Map<String, String> stringStringMap = new HashMap<>();
            stringStringMap.put(id, buffer);
            expression.expressionPartsPrecedenceLeveledMap.put(level.toString(), stringStringMap);
        }
    }
}

