package Entities;

import Enums.VariableType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class VariablesContainer {
    private Map<String, Variable> variableMap = new LinkedHashMap<>();

    public void add(Variable variable) throws Exception {
        if (variableMap.containsKey(variable.name)) {
            throw new Exception("Variable name already exist");
        }
        variableMap.put(variable.name, variable);
    }

    public Variable get(String varName) throws Exception {
        if (variableMap.containsKey(varName)) {
            return variableMap.get(varName);
        }
        throw new Exception("Variable is absent: " + varName);
    }

    public VariableType getType(String varName) {
        if (variableMap.containsKey(varName)) {
            Variable variable = variableMap.get(varName);
            return variable.type;
        }
        return null;
    }

    public void update(Variable variable) throws Exception {
        if (variableMap.containsKey(variable.name)) {
            variableMap.put(variable.name, variable);
        } else {
            throw new Exception("Variable name already exist");
        }
    }

    @Override
    public String toString() {
        return
                "\n" +
                        "values " + variableMap.values() +
                        " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariablesContainer that = (VariablesContainer) o;
        return Objects.equals(variableMap, that.variableMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableMap);
    }
}