package Entities;

import Enums.VariableType;

import java.util.Objects;

public class Operand extends ExpressionOperationPartType {
    public String value;
    public VariableType variableType;

    public Operand(String subStr) {
        value = subStr;
    }

    public Operand(String subStr, VariableType type) {
        value = subStr;
        variableType = type;
    }

    @Override
    public String toString() {
        return "Operand{" +
                "value='" + value + '\'' +
                ", variableType=" + variableType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operand operand = (Operand) o;
        return Objects.equals(value, operand.value) &&
                variableType == operand.variableType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, variableType);
    }
}