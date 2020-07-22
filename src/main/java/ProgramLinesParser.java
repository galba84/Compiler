import Dto.ProgramBlocksDto;
import Entities.ParserPointer;
import Exceptions.ProgramBlocksParsingException;

import java.util.Arrays;
import java.util.List;

public class ProgramLinesParser {
    ParserPointer currentParsingPoint;
    ProgramBlocksDto result;
    int lineCount;

    public ProgramBlocksDto parse(String text) {
        currentParsingPoint = ParserPointer.ZERO;
        result = new ProgramBlocksDto();
        lineCount = 0;

        String[] strings = text.split("\\r?\\n");
        for (String line : strings) {
            lineCount++;
            String res = parseLine(line);
            if (currentParsingPoint.equals(ParserPointer.VAR)) {
                saveVariables(res);
            }
            if (currentParsingPoint.equals(ParserPointer.BEGIN)) {
                saveBody(res);
            }
        }
        if (result.variablesBlock.size() > 0) {
            result.variablesBlock.remove(0);
        }
        if (result.bodyBlock.size() > 0) {
            result.bodyBlock.remove(0);
        }
        return result;
    }

    private String parseLine(String line) {
        try {
            validate(line);
            switchParsingPoint(line);
        } catch (ProgramBlocksParsingException e) {
            System.out.println(e.getMessage());
            return line;
        }
        return line;
    }

    private void switchParsingPoint(String line) {
        if (line.trim().equals("PROGRAM")) {
            currentParsingPoint = ParserPointer.PROGRAM;
        }
        if (line.trim().equals("VAR")) {
            currentParsingPoint = ParserPointer.VAR;
        }
        if (line.trim().equals("BEGIN")) {
            currentParsingPoint = ParserPointer.BEGIN;
        }
        if (line.trim().equals("END")) {
            currentParsingPoint = ParserPointer.END;
        }
    }

    private void validate(String line) throws ProgramBlocksParsingException {
        if (currentParsingPoint.equals(ParserPointer.ZERO)) {
            validateZero(line);
        }
        if (currentParsingPoint.equals(ParserPointer.PROGRAM)) {
            validateInitial(line);
        }
        if (currentParsingPoint.equals(ParserPointer.VAR)) {
            validateVariable(line);
        }
        if (currentParsingPoint.equals(ParserPointer.BEGIN)) {
            validateBody(line);
        }
        if (currentParsingPoint.equals(ParserPointer.END)) {
            validateEnd(line);
        }
    }

    private void validateBody(String line) throws ProgramBlocksParsingException {
        if (line.trim().isEmpty()) {
            return;
        }
        if (line.trim().equals("END")) {
            return;
        }
        if (isContainsBlockKeyWords(line)) {
            throw new ProgramBlocksParsingException("Error on line " + lineCount + " : " + line);
        }
    }

    private void validateVariable(String line) throws ProgramBlocksParsingException {
        if (line.trim().isEmpty()) {
            return;
        }
        if (line.trim().equals("BEGIN")) {
            return;
        }
        if (isContainsBlockKeyWords(line)) {
            throw new ProgramBlocksParsingException("Error on line " + lineCount + " : " + line);
        }
    }

    private boolean isContainsBlockKeyWords(String line) {
        String[] words = line.replace(',', ' ').split(" ");

        for (ParserPointer value : ParserPointer.values()
        ) {
            for (String word : words
            ) {
                if (value.name().equals(word)) {
                    return true;
                }
            }

        }
        return false;
    }

    private void validateInitial(String line) throws ProgramBlocksParsingException {
        if (line.trim().isEmpty()) {
            return;
        }
        if (!line.trim().equals("VAR")) {
            throw new ProgramBlocksParsingException("Error on line " + lineCount + " : " + line);
        }

    }

    private void validateEnd(String line) throws ProgramBlocksParsingException {
        if (!line.trim().isEmpty()) {
            throw new ProgramBlocksParsingException("Error on line " + lineCount + " : " + line);
        }
    }

    private void validateZero(String line) throws ProgramBlocksParsingException {
        if (line.trim().isEmpty()) {
            return;

        }
        if (!line.trim().equals("PROGRAM")) {
            throw new ProgramBlocksParsingException("Error on line " + lineCount + " : " + line);
        }
    }

    private void saveBody(String line) {
        result.bodyBlock.add(line);

    }

    private void saveVariables(String line) {
        result.variablesBlock.add(line);
    }
}
