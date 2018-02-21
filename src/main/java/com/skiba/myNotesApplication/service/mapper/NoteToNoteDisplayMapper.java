package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.NoteDisplay;
import com.skiba.myNotesApplication.model.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteToNoteDisplayMapper {

    public NoteDisplay map(Note note){

        Long id = note.getId();
        String text = note.getText();

        NoteDisplay noteDisplay = new NoteDisplay(id,text);

        return noteDisplay;
    }
}
