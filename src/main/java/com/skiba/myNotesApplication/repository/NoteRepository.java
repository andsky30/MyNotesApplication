package com.skiba.myNotesApplication.repository;

import com.skiba.myNotesApplication.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Note findNoteById(Long noteId);

    void deleteNoteById(Long noteId);

}
