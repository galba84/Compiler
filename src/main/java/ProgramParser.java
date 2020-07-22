import Dto.ProgramBlocksDto;
import Entities.*;
import Enums.ExpressionType;
import Enums.VariableType;
import Exceptions.ExpressionException;

import java.util.LinkedList;
import java.util.List;

public class ProgramParser {

    int lineNumber = 0;
    int id;

    int getId() {
        return ++id;
    }

    ExpressionParser expressionParser = new ExpressionParser();

    BodyParseState parseBodyState = BodyParseState.SINGLE_LINE;
    List<BodyElement> expressionBuffer;
    boolean multiLines = false;

    ProgramParser() {

    }

    public Program parse(ProgramBlocksDto blockLines) {
        Program result = new Program();
        parseVariablesBlock(blockLines, result);
        lineNumber = 0;
        parseBodyBlock(blockLines, result);
        return result;
    }

    private void parseBodyBlock(ProgramBlocksDto blockLines, Program result) {
        for (String line : blockLines.bodyBlock) {
            lineNumber++;
            try {
                BodyElement bodyElement = parseBody(line);
                if (null != bodyElement) {
                    result.flow.put("$" + getId(), bodyElement);
                }
            } catch (Exception e) {
                System.out.println("ERROR wrong line body " + lineNumber + " " + line + " " + e.getMessage());
            }
        }
    }

    private void parseVariablesBlock(ProgramBlocksDto blockLines, Program result) {
        for (String line : blockLines.variablesBlock) {
            lineNumber++;
            try {
                result.variablesContainer.add(parseVariables(line));
            } catch (Exception e) {
                System.out.println("wrongLine var " + lineNumber);
            }
        }
    }


    public BodyElement parseBody(String line) throws ExpressionException {
        if (null == line) {
            return null;
        }
        line=line.trim();
        BodyExpressionLineType bodyExpressionLineType = null;
        try {
            bodyExpressionLineType = switchBodyLineType(line);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        if (parseBodyState.equals(BodyParseState.SINGLE_LINE)) {
            if (bodyExpressionLineType.equals(BodyExpressionLineType.REGULAR)) {
                Expression expression = expressionParser.parse(line);
                expression.expressionType = ExpressionType.assignment;
//                expression.validate();
                return expression;
            }
            if (bodyExpressionLineType.equals(BodyExpressionLineType.CONDITION)) {
                parseBodyState = BodyParseState.CYCLE;
                expressionBuffer = new LinkedList<>();
                Expression expression = expressionParser.parseCondition(line);
                expression.expressionType = ExpressionType.condition;
//                expression.validate();
                expressionBuffer.add(expression);
                return null;
            }
        }
        if (parseBodyState.equals(BodyParseState.CYCLE)) {
            if (bodyExpressionLineType.equals(BodyExpressionLineType.OPEN_FIGURE_BRACKETS)) {
                multiLines = true;
                return null;
            }
            if (bodyExpressionLineType.equals(BodyExpressionLineType.CLOSE_FIGURE_BRACKETS)) {
                multiLines = false;
                Expression condition = (Expression) expressionBuffer.remove(0);
                Cycle cycle = new Cycle(condition, expressionBuffer);
                cycle.line = cycle.toString();
                parseBodyState = BodyParseState.SINGLE_LINE;
                return cycle;
            }
            if (bodyExpressionLineType.equals(BodyExpressionLineType.REGULAR)) {
                if (multiLines) {
                    Expression exp = expressionParser.parse(line);
                    exp.expressionType = ExpressionType.assignment;
//                    exp.validate();
                    expressionBuffer.add(exp);
                    return null;
                } else {
                    parseBodyState = BodyParseState.SINGLE_LINE;
                    Expression exp = expressionParser.parse(line);
                    exp.expressionType = ExpressionType.assignment;
                    exp.validate();
                    expressionBuffer.add(exp);
                    Expression condition = (Expression) expressionBuffer.remove(0);
                    Cycle cycle = new Cycle(condition, expressionBuffer);
                    cycle.line = line;
                    return cycle;
                }
            }
        }
        return null;
    }

    private BodyExpressionLineType switchBodyLineType(String line) throws Exception {
        String trimmedLine = line.trim();
        if (trimmedLine.equals("{")) {
            return BodyExpressionLineType.OPEN_FIGURE_BRACKETS;
        }
        if (trimmedLine.equals("}")) {
            return BodyExpressionLineType.CLOSE_FIGURE_BRACKETS;
        }
        if (trimmedLine.endsWith(";")) {
            return BodyExpressionLineType.REGULAR;
        }
        if (trimmedLine.startsWith("WHILE") && trimmedLine.endsWith(")")) {
            return BodyExpressionLineType.CONDITION;
        }
        throw new Exception("BodyExpressionLineType fail");
    }

    private Variable parseVariables(String s) {
        String[] s1 = s.split(" ");
        return new Variable(s1[1].replace(";", ""), "null", VariableType.valueOf(s1[0]));
    }

    public enum BodyParseState {
        SINGLE_LINE, CYCLE
    }

    public enum BodyExpressionLineType {
        REGULAR, CONDITION, OPEN_FIGURE_BRACKETS, CLOSE_FIGURE_BRACKETS
    }

}
