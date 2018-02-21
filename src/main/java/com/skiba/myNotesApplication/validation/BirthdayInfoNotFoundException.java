package com.skiba.myNotesApplication.validation;

public class BirthdayInfoNotFoundException extends RuntimeException {

    public static final String BIRTHDAY_NOT_FOUND_MESSAGE = "Could not find birthday with ID = %d.";

    public BirthdayInfoNotFoundException(Long birthdayId) {
        super(String.format(BIRTHDAY_NOT_FOUND_MESSAGE, birthdayId));
    }
}
