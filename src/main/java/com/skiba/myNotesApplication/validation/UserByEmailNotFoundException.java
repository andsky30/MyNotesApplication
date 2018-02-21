package com.skiba.myNotesApplication.validation;

//@ResponseStatus(value = HttpStatus.TEMPORARY_REDIRECT)
public class UserByEmailNotFoundException extends RuntimeException {

    public static final String USER_NOT_FOUND_MESSAGE = "Could not find user with e-mail = %s.";

    public UserByEmailNotFoundException(String email) {
        super(String.format(USER_NOT_FOUND_MESSAGE, email));
    }
}
