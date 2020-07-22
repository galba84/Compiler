package Entities;

import java.util.LinkedHashMap;
import java.util.Map;

public class Program {
    public VariablesContainer variablesContainer = new VariablesContainer();
    public Map<String, BodyElement> flow = new LinkedHashMap<>();

    String getValues() {
        String result = "";
        for (BodyElement b :
                flow.values()) {
            result += b + " \n";
        }
        return result;
    }

    @Override
    public String toString() {

        return "Program{" + "\n" +
                "variablesContainer=" + variablesContainer + "\n" +
                " flow keys:" + "\n" + flow.keySet() + "\n" +
                " flow values:" + "\n" +
                getValues() + "\n" +
                '}';
    }
}
