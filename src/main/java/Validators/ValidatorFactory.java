package Validators;

import Entities.ParserPointer;
import Entities.Variable;

public class ValidatorFactory {
    public static Validator getValidator(Variable variable) {
        return new VariableValidator();
    }

    public static Validator getLineValidator(ParserPointer var) throws Exception {
        switch (var) {
            case VAR: {
                return new VarLineValidator() {
                };
            }
        }
        throw new Exception("validator not found");
    }
}
