package com.skiba.myNotesApplication.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasNumberValidator implements ConstraintValidator<HasNumber,String>{


    @Override
    public void initialize(HasNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches(".*\\d+.*");
    }
}
