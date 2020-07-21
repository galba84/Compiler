package Entities;

import java.util.Objects;

public class Operator extends ExpressionOperationPartType implements Comparable<Operator> {
    public String value;
    private int precedence;

    public Operator(String subStr) {
        value = subStr;
        setPrecedence();
    }

    void setPrecedence() {
        switch (value) {
            case ":=":
                precedence = 4;
                break;
            case ">":
                precedence = 3;
                break;
            case "<":
                precedence = 3;
                break;
            case "=":
                precedence = 3;
                break;
            case "+":
                precedence = 2;
                break;
            case "-":
                precedence = 2;
                break;
            case "^":
                precedence = 1;
                break;
            case "*":
                precedence = 0;
                break;
            case "/":
                precedence = 0;
                break;
        }
    }

    public boolean isConditional() {
        switch (value) {
            case ">":
                precedence = 3;
                return true;
            case "<":
                precedence = 3;
                return true;
            case "=":
                precedence = 3;
                return true;
        }
        return false;
    }

    public boolean isAssignment() {
        switch (value) {
            case ":=":
                return true;
        }
        return false;
    }

    public boolean isArithmetical() {
        switch (value) {
            case "+":
                return true;
            case "-":
                return true;
            case "^":
                return true;
            case "*":
                return true;
            case "/":
                return true;
        }
        return false;
    }

    @Override
    public int compareTo(Operator operator) {
        return precedence - operator.precedence;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        return precedence == operator.precedence &&
                Objects.equals(value, operator.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, precedence);
    }
}