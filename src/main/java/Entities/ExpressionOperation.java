package Entities;

import Enums.VariableType;

import java.util.Objects;

public class ExpressionOperation implements Cloneable {

    Operand first;
    Operand second;
    Operator operator;

    public ExpressionOperation(Operand first, Operand second, Operator operator) {
        this.first = first;
        this.second = second;
        this.operator = operator;
    }

    public Operand getFirst() {
        return first;
    }

    public void setFirst(Operand first) {
        this.first = first;
    }

    public Operand getSecond() {
        return second;
    }

    public void setSecond(Operand second) {
        this.second = second;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionOperation that = (ExpressionOperation) o;
        return Objects.equals(first, that.first) &&
                Objects.equals(second, that.second) &&
                Objects.equals(operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, operator);
    }

    @Override
    public String toString() {
        return "" +
                "{" +
                "f=" + first +
                ", s=" + second +
                ", op=" + operator +
                '}';
    }

    public void setOperandsTypes(VariablesContainer variablesContainer) {
        setVarType(first, variablesContainer);
        setVarType(second, variablesContainer);

    }

    private void setVarType(Operand operand, VariablesContainer variablesContainer) {
        String s = operand.value;
        if (null != operand.variableType) {
            return;
        }

        if (null != variablesContainer) {
            VariableType varType = variablesContainer.getType(operand.value);
            if (null != varType)
                operand.variableType = varType;
        }

        if (s.equals("true") || s.equals("false")) {
            operand.variableType = VariableType.bool;
            return;
        }
        if (s.matches("[0-9]+")) {
            operand.variableType = VariableType.integer;
            return;
        }
        if (s.matches("[+-]?([0-9]*[.])?[0-9]+")) {
            operand.variableType = VariableType.real;
            return;
        }
        if (s.startsWith("\"") && s.endsWith("\"")) {
            operand.variableType = VariableType.string;
            return;
        }
    }

    public void validateReadyForMagic() throws Exception {
        if (!first.variableType.equals(second.variableType)) {
            System.out.println("type mismatch " + toString());
            throw new Exception("Execution exception :" + toString());
        }
        if (first.variableType.equals(VariableType.integer) || (first.variableType.equals(VariableType.real) && operator.isArithmetical() || operator.isAssignment())) {
            return;
        }
        if (first.variableType.equals(VariableType.bool) && operator.isConditional() || operator.isAssignment()) {
            return;
        }
        if (first.variableType.equals(VariableType.string) && operator.value.equals("+") ) {
            return;
        }
        throw new Exception("Operation not supported" + toString());
    }

    @Override
    public ExpressionOperation clone() {
        return new ExpressionOperation(new Operand(first.value, first.variableType), new Operand(second.value, second.variableType),
                new Operator(operator.value));
    }
}