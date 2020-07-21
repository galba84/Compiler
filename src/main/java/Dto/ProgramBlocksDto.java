package Dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ProgramBlocksDto {
    public List<String> initialBlock = new LinkedList<>();
    public List<String> variablesBlock =new LinkedList<>();
    public List<String> bodyBlock =new LinkedList<>();

    @Override
    public String toString() {
        return "ProgramBlocksDto\n" +
                "initialBlock=" + initialBlock +"\n"+
                "variablesBlock=" + variablesBlock +"\n" +
                "bodyBlock=" + bodyBlock +"\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramBlocksDto that = (ProgramBlocksDto) o;
        return Objects.equals(initialBlock, that.initialBlock) &&
                Objects.equals(variablesBlock, that.variablesBlock) &&
                Objects.equals(bodyBlock, that.bodyBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialBlock, variablesBlock, bodyBlock);
    }
}
