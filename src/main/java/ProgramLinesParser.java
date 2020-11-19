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

        List<String> lines = Arrays.asList(text.split("\\r?\\n"));
        lines.stream().map(this::parseLine).forEach(this::saveLineParseResult);

        if (result.variablesBlock.size() > 0) {
            removeBlockName(result.variablesBlock);
        }
        if (result.bodyBlock.size() > 0) {
            removeBlockName(result.bodyBlock);
        }
        return result;
    }

    private void removeBlockName(List<String> variablesBlock) {
        variablesBlock.remove(0);
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
        List<String> strings = Arrays.asList(line.replace(',', ' ').split(" "));
        return strings.stream().anyMatch(ParserPointer::contains);
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

    private void saveLineParseResult(String result){
        if (currentParsingPoint.equals(ParserPointer.VAR)) {
            saveVariables(result);
        } else if (currentParsingPoint.equals(ParserPointer.BEGIN)) {
            saveBody(result);
        }
    }
}
