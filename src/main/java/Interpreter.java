import Entities.*;
import Enums.ExpressionType;
import Enums.VariableType;
import Exceptions.ExpressionException;

import java.util.LinkedHashMap;
import java.util.Map;

public class Interpreter {
    VariablesContainer variablesContainer;
    Map<String, BodyElement> flow;
    Map<String, Operand> executionFlow = new LinkedHashMap<>();
    String currentOperationId;


    public void execute(Program program) {
        this.variablesContainer = program.variablesContainer;
        this.flow = program.flow;

        for (Map.Entry<String, BodyElement> entry : flow.entrySet()) {
            executeBodyElement(entry.getValue());
        }

        System.out.println("FINAL SUCESS");
        System.out.println("VAR VALUES : ");
        System.out.println(variablesContainer.toString());
    }

    private void executeBodyElement(BodyElement value) {
        if (value instanceof Expression) {
            Expression expression = (Expression) value;
            if (expression.expressionType.equals(ExpressionType.assignment)) {
                executeAssignment(expression);
            }
        } else if (value instanceof Cycle) {
            Cycle cycle = (Cycle) value;
            while (executeCondition(cycle.getCondition())) {
                for (BodyElement element :
                        cycle.getPositive()) {
                    Expression expression = (Expression) element;
                    if (expression.expressionType.equals(ExpressionType.assignment)) {
                        executeAssignment(expression);
                    }
                }
            }
        }
    }

    private boolean executeCondition(Expression condition) {
        try {
            condition.validate();
        } catch (ExpressionException e) {
            System.out.println("Condition vot valid" + condition.toString());
            System.exit(0);
        }
        ExpressionOperation operation = condition.orderedExpressionOperations.get(0);
        return executeLogicOperation(operation);
    }

    private boolean executeLogicOperation(ExpressionOperation operation) {
        ExpressionOperation expressionOperationCopy = null;
        try {
            expressionOperationCopy = operation.clone();
            updateOperationVariablesValues(expressionOperationCopy);
            expressionOperationCopy.setOperandsTypes(variablesContainer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return magicLogic(expressionOperationCopy);

    }

    private void executeAssignment(Expression expression) {
        if (expression.orderedExpressionOperations.size() < 1) {
            return;
        }
        int id = 0;
        for (ExpressionOperation operation :
                expression.orderedExpressionOperations) {
            currentOperationId = "$" + id;
            executeExpressionOperation(operation);
            id++;
        }
    }

    private void executeExpressionOperation(ExpressionOperation operation) {
        ExpressionOperation expressionOperationCopy;
        try {
            expressionOperationCopy = operation.clone();
            updateOperationVariablesValues(expressionOperationCopy);
            expressionOperationCopy.setOperandsTypes(variablesContainer);
            try {
                expressionOperationCopy.validateReadyForMagic();
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                System.exit(0);
            }
            magic(expressionOperationCopy);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("execution success" + operation.toString() + " var " + variablesContainer.toString());
        System.out.println("___________");
    }

    private void magic(ExpressionOperation operation) {
        if (operation.getOperator().isAssignment()) {
            try {
                Variable variable = variablesContainer.get(operation.getFirst().value);
                variable.value = operation.getSecond().value;
                if (!variable.type.equals(operation.getSecond().variableType)) {
                    System.out.println("Error type mismatch " + operation.toString());
                    return;
                }
                variablesContainer.update(variable);
            } catch (Exception e) {
                System.out.println("Error : no such var" + operation.getFirst().value);
            }
        } else if (isStringOperation (operation)){
            String first = operation.getFirst().value;
            String second = operation.getSecond().value;
            String result = executeString(first, second, operation.getOperator().value);
            executionFlow.put(currentOperationId, new Operand(result, VariableType.string));
        }

        else if (operation.getOperator().isArithmetical()) {
            if (operation.getFirst().variableType.equals(VariableType.integer)) {
                Integer first = Integer.parseInt(operation.getFirst().value);
                Integer second = Integer.parseInt(operation.getSecond().value);
                Integer result = executeArithmetic(first, second, operation.getOperator().value);
                executionFlow.put(currentOperationId, new Operand(result.toString(), VariableType.integer));
            }
        }
    }

    private String executeString(String first, String second, String value) {
        switch (value){
            case "+":{  return first+second;}
        }
      return null;
    }

    private boolean isStringOperation(ExpressionOperation operation) {
        return operation.getFirst().variableType.equals(VariableType.string)&&operation.getSecond().variableType.equals(VariableType.string);
    }

    private boolean magicLogic(ExpressionOperation operation) {
        if (operation.getFirst().variableType.equals(VariableType.integer)) {
            Integer first = Integer.parseInt(operation.getFirst().value);
            Integer second = Integer.parseInt(operation.getSecond().value);
            return executeConditionalInt(first, second, operation.getOperator().value);
        }
        if (operation.getFirst().variableType.equals(VariableType.string)) {
            String first = operation.getFirst().value;
            String second = operation.getSecond().value;
            return executeConditionalString(first, second, operation.getOperator().value);
        }
        if (operation.getFirst().variableType.equals(VariableType.bool)) {
            String first = operation.getFirst().value;
            String second = operation.getSecond().value;
            return executeConditionalBool(first, second, operation.getOperator().value);
        }
        return false;
    }

    private boolean executeConditionalString(String first, String second, String operator) {
        switch (operator) {
            case "=": {
                return first.equals(second);
            }
        }
        System.out.println("only '=' operator is allowed ");
        return false;
    }

    private boolean executeConditionalBool(String first, String second, String operator) {
        switch (operator) {
            case "=": {
                return first.equals(second);
            }
        }
        System.out.println("only '=' operator is allowed ");
        return false;
    }

    private boolean executeConditionalInt(Integer first, Integer second, String operator) {
        switch (operator) {
            case "=": {
                return first.equals(second);
            }
            case ">": {
                return first > second;
            }
            case "<": {
                return first < second;
            }
        }
        return false;
    }

    private void updateOperationVariablesValues(final ExpressionOperation operation) {
        Operand first = operation.getFirst();
        if (first.value.startsWith("$")) {
            Operand operand = executionFlow.get(first.value);
            first.value = operand.value;
            first.variableType = operand.variableType;
        } else if (first.value.substring(0, 1).matches("[a-z]") && (!operation.getOperator().isAssignment())) {
            Variable operand = null;
            try {
                operand = variablesContainer.get(first.value);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            first.value = operand.value;
            first.variableType = operand.type;
        } else if (first.value.substring(0, 1).matches("[a-z]") && (operation.getOperator().isAssignment())) {
            Variable operand = null;
            try {
                operand = variablesContainer.get(first.value);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            first.variableType = operand.type;

        }
        Operand second = operation.getSecond();
        if (second.value.startsWith("\"") || second.value.endsWith("\"")) {
            return;
        } else
            if (second.value.equals("true") || second.value.equals("false")) {
            return;
        } else if (second.value.startsWith("$")) {
            Operand operand = executionFlow.get(second.value);
            second.value = operand.value;
            second.variableType = operand.variableType;
        } else if (second.value.substring(0, 1).matches("[a-z]")) {
            Variable operand = null;
            try {
                operand = variablesContainer.get(second.value);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            second.value = operand.value;
        }
    }

    private Integer executeArithmetic(Integer first, Integer second, String operator) {
        switch (operator) {
            case "+":
                return first + second;
            case "-":
                return first - second;
            case "*":
                return first * second;
            case "/":
                return first / second;
            case ":=":
                return second;
        }
        try {
            throw new Exception("Error ExecuteArithmetic " + first + " " + second + " " + operator);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
