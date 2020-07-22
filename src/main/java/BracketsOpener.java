import java.util.*;

public class BracketsOpener {
    public Map<String, String> tempBuffer = new LinkedHashMap<>();

    public void openBrackets(String expressionString) {
        List<String> expressionSymbols = new LinkedList<>(Arrays.asList(expressionString.split("")));
        List<String> elements = splitByElements(expressionSymbols);
        StringBuffer buffer = new StringBuffer();
        Integer level = 0;
        for (String s :
                elements) {
            if (s.equals("(")) {
                if (buffer.length() > 0) {
                    putToMap(level, buffer.toString());
                }
                level++;
                buffer = new StringBuffer();
            } else if (s.equals(")")) {
                if (buffer.length() > 0) {
                    putToMap(level, buffer.toString());
                    buffer = new StringBuffer();
                }
                level--;
            } else {
                buffer.append(s);
            }

        }
        if (buffer.length() > 0) {
            putToMap(level, buffer.toString());
        }
    }

    private void putToMap(int index, String buffer) {
        int i = index + 1;

        if (tempBuffer.containsKey("$" + index)) {
            String s = tempBuffer.get("$" + index);
            s += "$" + i + buffer;
            tempBuffer.put("$" + index, s);
        } else {
            if (tempBuffer.containsKey("$" + (index + 1))) {
                String s = "$" + i + buffer;
                tempBuffer.put("$" + index, s);

            } else {
                tempBuffer.put("$" + index, buffer);
            }
        }

    }

    public List<String> splitByElements(List<String> expressionSymbols) {
        List<String> result = new LinkedList<>();
        String buffer = "";
        String bufferChatType = "";
        Character character ;
        for (String expressionSymbol : expressionSymbols) {
            character = expressionSymbol.charAt(0);
            if (isLatinLetter(character)) {
                if (buffer.length() > 0) {
                    if (bufferChatType.equals("char")) {
                        buffer += character;
                    } else {
                        result.add(buffer);
                        buffer = character.toString();
                        bufferChatType = "char";
                    }
                } else {
                    bufferChatType = "char";
                    buffer += character;
                }
            } else {
                bufferChatType = "";
                if (buffer.length() > 0) {
                    result.add(buffer);
                    buffer = "";
                }
                result.add(character.toString());
            }
        }
        if (buffer.length() > 0) {
            result.add(buffer);
        }
        for (int i = 0; i < result.size(); i++) {
            String s = result.get(i);
            if (s.equals(":")) {
                if (i + 1 <= result.size()) {
                    String ss = result.get(i + 1);
                    String buf = result.get(i) + result.get(i + 1);
                    result.set(i, buf);
                    result.remove(i + 1);
                }
            }
        }
        return result;
    }

    public static boolean isLatinLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (Character.isDigit(c));
    }
}
