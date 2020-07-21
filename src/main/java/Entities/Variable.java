package Entities;

import Enums.VariableType;

import java.util.Objects;

public class Variable extends BodyElement {
    public String name;
    public String value;
    public VariableType type;

    public Variable(String name, String value, VariableType type) {
        this.name = name.trim();
        this.value = value;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name) &&
                Objects.equals(value, variable.value) &&
                type == variable.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, type);
    }
}