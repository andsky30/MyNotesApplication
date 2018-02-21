package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.NoteDisplay;
import com.skiba.myNotesApplication.model.Note;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class NoteToNoteDisplayMapperTest {

    private static final String NOTE_TEXT = "Jak Kuba Bogu tak Bóg Kubie";

    NoteToNoteDisplayMapper noteToNoteDisplayMapper = new NoteToNoteDisplayMapper();

    @Test
    public void shouldReturnNoteDisplay() {

        //given
        Note note = new Note();
        note.setId(25L);
        note.setText(NOTE_TEXT);

        //when
        NoteDisplay noteDisplay = noteToNoteDisplayMapper.map(note);

        //then
        assertThat(noteDisplay).isNotNull();
        assertThat(noteDisplay.getText()).isEqualTo(NOTE_TEXT);
    }

}