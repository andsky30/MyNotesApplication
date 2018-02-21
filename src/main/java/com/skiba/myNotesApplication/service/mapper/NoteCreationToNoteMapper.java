package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.NoteCreation;
import com.skiba.myNotesApplication.model.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteCreationToNoteMapper {

    public Note map(NoteCreation noteCreation){

        Note note = new Note();
        note.setText(noteCreation.getText());

        return note;
    }

}
