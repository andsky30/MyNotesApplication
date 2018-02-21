package com.skiba.myNotesApplication.validation;

public class UserByIdNotFoundException extends RuntimeException {

    public static final String USER_BY_ID_NOT_FOUND_MESSAGE = "Could not find user with id = %d.";

    public UserByIdNotFoundException(Long userId) {
        super(String.format(USER_BY_ID_NOT_FOUND_MESSAGE, userId));
    }
}
