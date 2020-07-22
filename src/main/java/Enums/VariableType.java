package Enums;

public enum VariableType {
    integer, real, string, bool;

    public static boolean contains(String test) {

        for (VariableType c : VariableType.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}