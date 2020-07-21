package Entities;

import Enums.ExpressionType;
import Exceptions.ExpressionException;

import java.util.*;

public class Expression extends BodyElement {
    public ExpressionType expressionType;
    public List<ExpressionOperation> orderedExpressionOperations = new LinkedList<>();
    public Map<String, Map<String, String>> expressionPartsPrecedenceLeveledMap = new LinkedHashMap<>();

    public List<Operator> getOperators(List<ExpressionOperationPartType> expressionOperationPartTypeList) {
        List<Operator> result = new LinkedList<>();
        for (ExpressionOperationPartType e : expressionOperationPartTypeList
        ) {
            if (e.getClass() == Operator.class) {
                result.add((Operator) e);
            }
        }
        return result;
    }

    public Map<String, ExpressionOperation> convertExpressionPartsIntoOrderedExpressionOperations(List<ExpressionOperationPartType> expressionOperationPartsList) {
        Map<String, ExpressionOperation> expressionOperationMap = new LinkedHashMap<>();
        List<Operator> operators = getOperators(expressionOperationPartsList);
        Collections.sort(operators);
        for (Operator o : operators) {
            int index = expressionOperationPartsList.indexOf(o);
            String id = "$" + getOperationIdForElementary();
            Operator operator = (Operator) expressionOperationPartsList.get(index);
            Operand first = (Operand) expressionOperationPartsList.get(index - 1);
            Operand second = (Operand) expressionOperationPartsList.get(index + 1);

            ExpressionOperation expressionOperation = new ExpressionOperation(first, second, operator);
            expressionOperationPartsList.set(index - 1, new Operand(id));
            expressionOperationPartsList.remove(index);
            expressionOperationPartsList.remove(index);

            expressionOperationMap.put(id, expressionOperation);
        }
        return expressionOperationMap;
    }

    public String getOperationId() {
        Integer res = ++id;
        return res.toString();
    }

    public String getOperationIdForElementary() {
        Integer res = idForElementaryOreration++;
        return res.toString();
    }

    @Override
    public String toString() {
        return "Expression{" +
                "orderedExpressionOperations=" + orderedExpressionOperations +
                ", id=" + id +
                '}';
    }

    public void validate() throws ExpressionException {
        if (expressionType.equals(ExpressionType.assignment)) {
            validateAssignment();
        }
        if (expressionType.equals(ExpressionType.condition)) {
            validateCondition();
        }
    }

    private void validateCondition() throws ExpressionException {
        ExpressionOperation expressionOperation = orderedExpressionOperations.get(0);
        if (orderedExpressionOperations.size() == 0 || (!expressionOperation.operator.isConditional() || orderedExpressionOperations.size() > 2)) {
            throw new ExpressionException("wrong " + line);
        }
    }

    private void validateAssignment() throws ExpressionException {
        if (null != orderedExpressionOperations && orderedExpressionOperations.size() > 0) {
            for (int i = 0; i < orderedExpressionOperations.size(); i++) {
                ExpressionOperation operation = orderedExpressionOperations.get(i);
                if (i + 1 == orderedExpressionOperations.size()) {
                    if (operation.operator.isAssignment()) {
                        return;
                    } else {
                        throw new ExpressionException("expect  assignment operation " + operation.toString());
                    }
                }
                if (operation.operator.isArithmetical()) {
                    continue;
                } else if (operation.operator.isConditional()) {
                    throw new ExpressionException("expect  arithmetic operation " + operation.toString());
                }
            }
        }
    }

}