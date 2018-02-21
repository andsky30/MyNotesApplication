package com.skiba.myNotesApplication.validation.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.skiba.myNotesApplication.service.mapper.BirthdayInfoToBirthdayDisplayMapper.BIRTHDAY_DATE_PATTERN;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateFormatValidator.class)
@Documented
public @interface DateFormat {

    String DATE_FORMAT_VALIDATOR_MESSAGE = "Date has to be in correct date format: " + BIRTHDAY_DATE_PATTERN;

    String message() default DATE_FORMAT_VALIDATOR_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}