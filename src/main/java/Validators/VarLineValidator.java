package Validators;

import Enums.VariableType;

import java.util.Arrays;
import java.util.List;


public class VarLineValidator implements Validator {
    public <T> void validate(T param) throws Exception {
        if(!(param instanceof String)){
            throw new Exception("Validation error. Can't validate " + param.getClass().getName());
        }
        String line = (String) param;
        String trimedLine = line.trim();
        if (trimedLine.length() == 0) {
            throw new Exception("line should not be empty");
        }
        List<String> varLineItems = Arrays.asList(trimedLine.split(" "));

        if (varLineItems.size() < 2) {
            throw new Exception("line should starts with VarType followed by var name enclosed by semicolon");
        }
        validateVarType(varLineItems.get(0));

        if (varLineItems.size() == 2) {
            String secondParam = varLineItems.get(1);
            String varName = secondParam.substring(0, secondParam.length() - 1);
            if (secondParam.length() > 1) {
                String thirdParam = secondParam.substring(secondParam.length() - 1);
                validateVarName(varName);
                validateSemicolon(thirdParam);
            } else {
                validateSemicolon(secondParam);
                validateVarName(secondParam);
            }

        } else if (varLineItems.size() == 3) {
            String secondParam = varLineItems.get(1);
            validateVarName(secondParam);
            String thirdParam = secondParam.substring(secondParam.length() - 1);
            validateSemicolon(thirdParam);
        }
    }

    private void validateSemicolon(String thirdParam) throws Exception {
        if (!";".equals(thirdParam)) {
            throw new Exception("line should be enclosed with semicolon");
        }
    }

    private void validateVarType(String varType) throws Exception {
        if (!VariableType.contains(varType)) {
            throw new Exception("Line starts not with VarType, line should starts with VarType followed by var name enclosed by semicolon");
        }
    }

    private void validateVarName(String varName) throws Exception {
        if (!varName.matches("^[a-z_]\\w*$")) {
            throw new Exception("variable should satisfy regex ^[a-z_]w*$");
        }
    }
}
