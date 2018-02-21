package com.skiba.myNotesApplication.validation.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HasNumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface HasNumber {

    String HAS_NUMBER_MESSAGE = "Password has to contain at least one number!";
    String message() default HAS_NUMBER_MESSAGE;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
