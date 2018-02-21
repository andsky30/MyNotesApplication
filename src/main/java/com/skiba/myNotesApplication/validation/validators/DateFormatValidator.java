package com.skiba.myNotesApplication.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static com.skiba.myNotesApplication.service.mapper.BirthdayInfoToBirthdayDisplayMapper.BIRTHDAY_DATE_FORMATTER;

public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

    @Override
    public void initialize(DateFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(String dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate.parse(dateOfBirth, BIRTHDAY_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;

    }
}
