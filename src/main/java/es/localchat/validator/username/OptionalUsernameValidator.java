package es.localchat.validator.username;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class OptionalUsernameValidator implements ConstraintValidator<ValidUsername, Optional<String>> {

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(Optional<String> value, ConstraintValidatorContext context) {
        if (value.isEmpty() || !value.get().matches("^[a-zA-Z0-9_]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only a-z, 0-9, and underscores are allowed.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
