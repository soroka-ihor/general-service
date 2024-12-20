package es.localchat.validator.uuid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class UUIDValidator implements ConstraintValidator<ValidUUID, Optional<String>> {

    private static final String PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    @Override
    public boolean isValid(Optional<String> uuid, ConstraintValidatorContext constraintValidatorContext) {
        if (uuid == null) {
            // Means client doesn't want to reply to any message, so we pass the validation
            return true;
        }
        if (!uuid.get().matches(PATTERN)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("UUID is not valid.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
