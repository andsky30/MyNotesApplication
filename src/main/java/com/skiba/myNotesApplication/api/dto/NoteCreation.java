package com.skiba.myNotesApplication.api.dto;

import org.hibernate.validator.constraints.NotBlank;


import javax.validation.constraints.Size;

public class NoteCreation {

    public static final String NOTE_TEXT_NOT_BLANK_MESSAGE = "Note text cannot be blank!";
    public static final String NOTE_TEXT_SIZE_MESSAGE = "Note should have max 500 characters!";


    @NotBlank(message = NOTE_TEXT_NOT_BLANK_MESSAGE)
    @Size(max = 500, message = NOTE_TEXT_SIZE_MESSAGE)
    private String text;

    public NoteCreation(String text) {
        this.text = text;
    }

    public NoteCreation(){}

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteCreation that = (NoteCreation) o;

        return text != null ? text.equals(that.text) : that.text == null;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NoteCreation{" +
                "text='" + text + '\'' +
                '}';
    }
}
