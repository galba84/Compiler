import Dto.ProgramBlocksDto;
import Entities.*;
import Enums.ExpressionType;
import Exceptions.ExpressionException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProgramEntitiesParser {

    int lineNumber;
    int id;
    Integer cycleLevelId = 0;
    Integer bracketsLevelId = 0;

    ExpressionParser expressionParser = new ExpressionParser();
    BodyParseState parseBodyState = BodyParseState.SINGLE_LINE;
    List<BodyElement> expressionBuffer;
    boolean multiLines = false;
    VariablesParser variablesParser = new VariablesParser();
    Map<String, Cycle> tempCycleElements = new LinkedHashMap<>();

    int getId() {
        return ++id;
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
                Variable variable = parseVariables(line);
                if (null != variable) {
                    result.variablesContainer.add(variable);
                }
            } catch (Exception e) {
                System.out.println("wrongLine var " + lineNumber);
            }
        }
    }


    public BodyElement parseBody(String line) throws ExpressionException {
        if (null == line && line.isEmpty()) {
            return null;
        }
        line = line.trim();
        BodyExpressionLineType bodyExpressionLineType = null;
        try {
            bodyExpressionLineType = switchBodyLineType(line);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        if (parseBodyState.equals(BodyParseState.SINGLE_LINE)) {
            return parseSingleBodyLine(line, bodyExpressionLineType);
        }
        if (parseBodyState.equals(BodyParseState.CYCLE)) {
            if (bodyExpressionLineType.equals(BodyExpressionLineType.OPEN_FIGURE_BRACKETS)) {
                bracketsLevelId++;
                multiLines = true;
                return null;
            }
            if (bodyExpressionLineType.equals(BodyExpressionLineType.CLOSE_FIGURE_BRACKETS)) {
                bracketsLevelId--;
                Expression condition = (Expression) expressionBuffer.remove(0);
                Cycle cycle = new Cycle(condition, expressionBuffer);
                cycle.line = cycle.toString();
                expressionBuffer.clear();
                if (cycleLevelId.equals(0)) {
                    parseBodyState = BodyParseState.SINGLE_LINE;
                    expressionBuffer.clear();
                    return cycle;
                }
                if (cycleLevelId > 0) {
                    Integer key = cycleLevelId - 1;
                    Cycle outerCycle = tempCycleElements.get(key.toString());
                    expressionBuffer.add(outerCycle.getCondition());
                    expressionBuffer.addAll(outerCycle.getPositive());
                    expressionBuffer.add(cycle);
                }
                cycleLevelId--;

                if (bracketsLevelId .equals(0) ) {
                    multiLines = false;
                }
            }
            if (bodyExpressionLineType.equals(BodyExpressionLineType.REGULAR)) {
                if (multiLines || bracketsLevelId>0) {
                    Expression exp = expressionParser.parse(line);
                    exp.expressionType = ExpressionType.assignment;
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
            } else if (bodyExpressionLineType.equals(BodyExpressionLineType.CONDITION)) {
                Expression condition = (Expression) expressionBuffer.remove(0);
                Cycle cycle = new Cycle(condition, expressionBuffer);
                cycle.line = cycle.toString();
                tempCycleElements.put(cycleLevelId.toString(), cycle);
                cycleLevelId++;
                expressionBuffer.clear();
                Expression expression = expressionParser.parseCondition(line);
                expression.expressionType = ExpressionType.condition;
                expressionBuffer.add(expression);
                System.out.println(line + "inside condition # " + cycleLevelId);
            }
        }
        System.out.println("line parse exception : " + line);
        return null;
    }

    private BodyElement parseSingleBodyLine(String line, BodyExpressionLineType bodyExpressionLineType) throws ExpressionException {
        if (bodyExpressionLineType.equals(BodyExpressionLineType.REGULAR)) {
            Expression expression = expressionParser.parse(line);
            expression.expressionType = ExpressionType.assignment;
            return expression;
        }
        if (bodyExpressionLineType.equals(BodyExpressionLineType.CONDITION)) {
            parseBodyState = BodyParseState.CYCLE;
            expressionBuffer = new LinkedList<>();
            Expression expression = expressionParser.parseCondition(line);
            expression.expressionType = ExpressionType.condition;
            expressionBuffer.add(expression);
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
        try {
            return variablesParser.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum BodyParseState {
        SINGLE_LINE, CYCLE
    }

    public enum BodyExpressionLineType {
        REGULAR, CONDITION, OPEN_FIGURE_BRACKETS, CLOSE_FIGURE_BRACKETS
    }

}
