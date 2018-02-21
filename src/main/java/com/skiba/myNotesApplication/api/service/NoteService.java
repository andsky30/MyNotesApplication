package com.skiba.myNotesApplication.api.service;

import com.skiba.myNotesApplication.api.dto.NoteCreation;
import com.skiba.myNotesApplication.api.dto.NoteDisplay;

import java.util.Set;

public interface NoteService {

    Set<NoteDisplay> getNotesForUser(String email);

    NoteDisplay getSingleNoteForUser(Long noteId, String email);

    NoteDisplay addNoteForUser(NoteCreation noteCreation, String email);

    void removeNote(Long noteId, String email);

    NoteDisplay updateNote(NoteCreation note, Long noteId, String email);

}
