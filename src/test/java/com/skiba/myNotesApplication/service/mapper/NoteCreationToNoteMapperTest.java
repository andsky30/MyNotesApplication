package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.NoteCreation;
import com.skiba.myNotesApplication.model.Note;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class NoteCreationToNoteMapperTest {

    private final static String NOTE_TEXT = "Gdyby kózka nie skakała, to by nóżki nie złamamła";

    NoteCreationToNoteMapper noteCreationToNoteMapper = new NoteCreationToNoteMapper();

    @Test
    public void shouldReturnNote() {

        //given
        NoteCreation noteCreation = new NoteCreation(NOTE_TEXT);

        //when
        Note note = noteCreationToNoteMapper.map(noteCreation);

        //then
        assertThat(note).isNotNull();
        assertThat(note.getText()).isEqualTo(NOTE_TEXT);
    }

}