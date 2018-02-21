package com.skiba.myNotesApplication.validation.validators;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.skiba.myNotesApplication.service.mapper.BirthdayInfoToBirthdayDisplayMapper.BIRTHDAY_DATE_FORMATTER;

public class PastValidator implements ConstraintValidator<PastStringDate, String> {

    @Override
    public void initialize(PastStringDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {

        boolean flag = false;

        try {
            LocalDate localDate = LocalDate.parse(date, BIRTHDAY_DATE_FORMATTER);
            if (localDate.isBefore(LocalDate.now())) {
                flag = true;
            } else
                flag = false;
        } catch (Exception e) {
            System.out.println("Could not check if date was from the past because of incorrect format!");
        }

        return flag;

    }
}