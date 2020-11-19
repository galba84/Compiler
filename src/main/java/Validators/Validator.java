package Validators;

public interface Validator {

    <T> void validate(T param) throws Exception;
}
