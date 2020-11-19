import Entities.ParserPointer;
import Entities.Variable;
import Enums.VariableType;
import Validators.Validator;
import Validators.ValidatorFactory;

import java.util.Arrays;
import java.util.List;

public class VariablesParser {

    public Variable parse(final String line) throws Exception {
        Validator lineValidator = ValidatorFactory.getLineValidator(ParserPointer.VAR);
        lineValidator.validate(line);
        String newLine = normalizeLine(line);
        List<String> varLineItems = Arrays.asList(newLine.trim().split(" "));
        return new Variable(varLineItems.get(1), "", VariableType.valueOf(varLineItems.get(0)));
    }

    public String normalizeLine(final String line) {
        String result = line.trim();
        if (result.indexOf(";") == result.length() - 1) {
            return result.substring(0, result.indexOf(";")).trim();
        }
        return result;
    }
}
