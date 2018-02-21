package com.skiba.myNotesApplication.api.controller;

import com.skiba.myNotesApplication.api.dto.NoteCreation;
import com.skiba.myNotesApplication.api.dto.NoteDisplay;
import com.skiba.myNotesApplication.api.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.Set;

@RestController
public class NoteController {

    public static final String MESSAGE_AFTER_NOTE_DELETION = "Note with ID: %d has been deleted successfully!!!";

    NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping(value = "/api/notes")
    public ResponseEntity<Set<NoteDisplay>> getNotesForUser(Principal principal) {

        Set<NoteDisplay> notes = noteService.getNotesForUser(principal.getName());
        return ResponseEntity.ok(notes);
    }

    @GetMapping(value = "/api/notes/{noteId}")
    public ResponseEntity<NoteDisplay> getSingleNote(@PathVariable Long noteId, Principal principal) {

        String email = principal.getName();

        NoteDisplay noteDisplay = noteService.getSingleNoteForUser(noteId, email);
        return ResponseEntity.ok(noteDisplay);
    }

    @PostMapping("/api/notes")
    public ResponseEntity<NoteDisplay> addNote(@RequestBody @Valid NoteCreation noteCreation,
                                               Principal principal) {

        String email = principal.getName();

        NoteDisplay saved = noteService.addNoteForUser(noteCreation, email);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(saved);
    }

    @PutMapping(value = "/api/notes/{noteId}")
    public ResponseEntity<NoteDisplay> updateNote(
            @RequestBody @Valid NoteCreation noteCreation, @PathVariable Long noteId, Principal principal) {

        String email = principal.getName();

        NoteDisplay updatedNote = noteService.updateNote(noteCreation, noteId, email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedNote);
    }

    @DeleteMapping(value = "/api/notes/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable Long noteId, Principal principal) {

        String email = principal.getName();

        noteService.removeNote(noteId, email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format(MESSAGE_AFTER_NOTE_DELETION, noteId));
    }
}
