package com.skiba.myNotesApplication.service.impl;

import com.skiba.myNotesApplication.api.dto.NoteCreation;
import com.skiba.myNotesApplication.api.dto.NoteDisplay;
import com.skiba.myNotesApplication.api.service.NoteService;
import com.skiba.myNotesApplication.model.Note;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.NoteRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.service.mapper.NoteCreationToNoteMapper;
import com.skiba.myNotesApplication.service.mapper.NoteToNoteDisplayMapper;
import com.skiba.myNotesApplication.validation.NoteNotFoundException;
import com.skiba.myNotesApplication.validation.UserByEmailNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private UserRepository userRepository;
    private NoteRepository noteRepository;
    private NoteToNoteDisplayMapper noteToNoteDisplayMapper;
    private NoteCreationToNoteMapper noteCreationToNoteMapper;

    @Autowired
    public NoteServiceImpl(UserRepository userRepository, NoteRepository noteRepository,
                           NoteCreationToNoteMapper noteCreationToNoteMapper,
                           NoteToNoteDisplayMapper noteToNoteDisplayMapper) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.noteCreationToNoteMapper = noteCreationToNoteMapper;
        this.noteToNoteDisplayMapper = noteToNoteDisplayMapper;
    }

    @Override
    public Set<NoteDisplay> getNotesForUser(String email) {

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserByEmailNotFoundException(email);
        } else {
            return user.getNotes()
                    .stream()
                    .map(noteToNoteDisplayMapper::map)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public NoteDisplay getSingleNoteForUser(Long noteId, String email) {
        Optional<Note> noteOptional = getNoteIfUserIsItsOwnerOptional(noteId, email);
        if (!noteOptional.isPresent()) {
            throw new NoteNotFoundException(noteId);
        } else {
            return noteToNoteDisplayMapper.map(noteOptional.get());
        }
    }

    @Override
    public NoteDisplay addNoteForUser(NoteCreation noteCreation, String email) {

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new UserByEmailNotFoundException(email);
        } else {
            Note note = noteCreationToNoteMapper.map(noteCreation);
            note.setUser(user);
            noteRepository.save(note);

            return noteToNoteDisplayMapper.map(note);
        }
    }

    @Override
    public NoteDisplay updateNote(NoteCreation noteCreation, Long noteId, String email) {

        Optional<Note> noteOptional = getNoteIfUserIsItsOwnerOptional(noteId, email);

        if (!noteOptional.isPresent()) {
            throw new NoteNotFoundException(noteId);
        } else {
            Note oldNote = noteOptional.get();
            oldNote.setText(noteCreation.getText());
            Note savedNote = noteRepository.save(oldNote);

            return noteToNoteDisplayMapper.map(savedNote);
        }
    }

    @Override
    public void removeNote(Long noteId, String email) {

        Optional<Note> noteOptional = getNoteIfUserIsItsOwnerOptional(noteId, email);
        if (!noteOptional.isPresent()) {
            throw new NoteNotFoundException(noteId);
        } else {
            Note note = noteOptional.get();
            note.getUser().getNotes().remove(note);
            note.setUser(null);
            noteRepository.delete(note);
        }
    }

    private Optional<Note> getNoteIfUserIsItsOwnerOptional(Long noteId, String email) {

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserByEmailNotFoundException(email);
        } else {
            return user
                    .getNotes()
                    .stream()
                    .filter(n -> n.getId() .equals( noteId))
                    .findFirst();
        }
    }
}


