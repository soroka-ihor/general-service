package es.localchat.validator.uuid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UUIDValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
    String message() default "{UUID}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
