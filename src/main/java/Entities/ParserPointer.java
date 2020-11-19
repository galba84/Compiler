package Entities;

public enum ParserPointer {
    ZERO, PROGRAM, VAR, BEGIN, END;

    public static boolean contains(String test) {
        for (ParserPointer c : ParserPointer.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
