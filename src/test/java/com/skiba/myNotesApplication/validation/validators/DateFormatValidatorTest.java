package com.skiba.myNotesApplication.validation.validators;

import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;


public class DateFormatValidatorTest {

    private static final ConstraintValidatorContext ANY_CONTEXT = null;
    private final DateFormatValidator validator = new DateFormatValidator();

    @Test
    public void shouldPassWhenValidDateProvided() {

        //given
        String correctDate = "2093-01-02";

        //when
        final boolean valid = validator.isValid(correctDate, ANY_CONTEXT);

        //then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldNotPassWhenInValidDateProvided() {

        //given
        String correctDate = "2093-41-22";

        //when
        final boolean valid = validator.isValid(correctDate, ANY_CONTEXT);

        //then
        assertThat(valid).isFalse();
    }

}