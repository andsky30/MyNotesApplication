package com.skiba.myNotesApplication.validation;

public class NoteNotFoundException extends RuntimeException {

    public static final String NOTE_NOT_FOUND_MESSAGE = "Could not find note with ID = %d.";

    public NoteNotFoundException(Long noteId) {
        super(String.format(NOTE_NOT_FOUND_MESSAGE, noteId));
    }
}
