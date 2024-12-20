package es.localchat.validator.fullname;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class OptionalFullNameValidator implements ConstraintValidator<ValidFullName, Optional<String>> {


    @Override
    public void initialize(ValidFullName constraintAnnotation) {

    }

    @Override
    public boolean isValid(Optional<String> value, ConstraintValidatorContext context) {
        if (value.isEmpty() || !value.get().matches("^[a-zA-Z]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only a-z are allowed.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
