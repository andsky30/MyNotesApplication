package com.skiba.myNotesApplication.service.impl;

import com.skiba.myNotesApplication.api.dto.NoteCreation;
import com.skiba.myNotesApplication.api.dto.NoteDisplay;
import com.skiba.myNotesApplication.model.Note;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.NoteRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.api.service.NoteService;


import com.skiba.myNotesApplication.service.mapper.NoteCreationToNoteMapper;
import com.skiba.myNotesApplication.service.mapper.NoteToNoteDisplayMapper;
import com.skiba.myNotesApplication.validation.NoteNotFoundException;
import com.skiba.myNotesApplication.validation.UserByEmailNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceImplTest {

    private static final Long NOTE_1_ID = 1L;
    private static final Long NOTE_2_ID = 2L;
    private static final Long NOT_EXISTING_NOTE_ID = 250L;
    private static final String NOTE_TEXT1 = "Kolor zielony";
    private static final String NOTE_TEXT2 = "Kolor niebieski";
    private static final int SET_SIZE_1 = 1;
    private static final int SET_SIZE_2 = 2;
    private static final int SET_SIZE_3 = 3;
    private static final String USER_EMAIL = "email@test.pjn";
    private static final String NOT_EXISTING_USER_MAIL = "notexisting@usermail.pl";

    @Mock
    UserRepository userRepository;

    @Mock
    NoteRepository noteRepository;

    NoteToNoteDisplayMapper noteToNoteDisplayMapper = new NoteToNoteDisplayMapper();
    NoteCreationToNoteMapper noteCreationToNoteMapper = new NoteCreationToNoteMapper();

    @InjectMocks
    private NoteService noteService = new NoteServiceImpl(userRepository, noteRepository,
            noteCreationToNoteMapper, noteToNoteDisplayMapper);

    private User user;
    private Set<Note> notes;


    @Before
    public void initializeUserAndNoteSet() {
        user = new User();
        user.setFirstName("Billy");
        user.setLastName("Thornton");
        user.setPassword("pass");
        user.setEmail(USER_EMAIL);

        notes = new HashSet<>();
        user.setNotes(notes);
    }

    @Test
    public void shouldReturnTwoNoteDisplays() {

        //given
        Note n1 = createOneNote(NOTE_1_ID, NOTE_TEXT1);
        notes.add(n1);

        Note n2 = createOneNote(NOTE_2_ID, NOTE_TEXT2);
        notes.add(n2);

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);

        //when
        Set<NoteDisplay> notesForUserSet = noteService.getNotesForUser(USER_EMAIL);

        //then
        assertThat(notesForUserSet).isNotNull();
        assertThat(notesForUserSet).hasSize(SET_SIZE_2);

        Optional<NoteDisplay> returnedSecondNoteDisplayOptional = notesForUserSet
                .stream()
                .filter(nd -> nd.getId() == NOTE_2_ID)
                .findFirst();
        returnedSecondNoteDisplayOptional.ifPresent(noteDisplay -> {
            assertThat(noteDisplay.getText()).isEqualTo(NOTE_TEXT2);
        });
    }

    public Note createOneNote(Long noteId, String noteText) {
        Note note = new Note();
        note.setId(noteId);
        note.setText(noteText);
        return note;
    }

    @Test(expected = UserByEmailNotFoundException.class)
    public void shouldNotAllowToInsertInvalidUserId() {
        //given

        //when
        noteService.getNotesForUser(USER_EMAIL);
        //then
    }

    @Test
    public void shouldReturnSingleNoteById() {

        //given
        Note note = createOneNote(NOTE_1_ID, NOTE_TEXT1);
        notes.add(note);

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);
        when(noteRepository.findNoteById(NOTE_1_ID)).thenReturn(note);

        //when
        NoteDisplay noteDisplay = noteService.getSingleNoteForUser(NOTE_1_ID, USER_EMAIL);

        //then
        assertThat(noteDisplay).isNotNull();
        assertThat(noteDisplay.getId()).isEqualTo(NOTE_1_ID);
        assertThat(noteDisplay.getText()).isEqualTo(NOTE_TEXT1);
    }

    @Test(expected = NoteNotFoundException.class)
    public void shouldNotAllowToInsertInvalidNoteId() {

        //given
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);

        //when
        noteService.getSingleNoteForUser(NOT_EXISTING_NOTE_ID, USER_EMAIL);

        //then
    }

    @Test(expected = UserByEmailNotFoundException.class)
    public void shouldNotAllowToInsertInvalidUserEmailForSingleNote() {

        //given

        //when
        noteService.getSingleNoteForUser(NOT_EXISTING_NOTE_ID, NOT_EXISTING_USER_MAIL);
        //then
    }

    @Test
    public void shouldAddOneNoteForUser() {

        //given
        NoteCreation noteCreation = new NoteCreation(NOTE_TEXT2);

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);

        //when
        NoteDisplay returnedNote = noteService.addNoteForUser(noteCreation, USER_EMAIL);

        //then
        assertThat(returnedNote).isNotNull();
        assertThat(notes).hasSize(SET_SIZE_1);

        assertThat(returnedNote.getText()).isEqualTo(NOTE_TEXT2);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldUpdateNoteText() {

        //given
        Note note = createOneNote(NOTE_1_ID, NOTE_TEXT1);
        notes.add(note);

        NoteCreation updateNote = new NoteCreation(NOTE_TEXT2);

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);
        when(noteRepository.findNoteById(NOTE_1_ID)).thenReturn(note);
        when(noteRepository.save(note)).thenReturn(note);

        //when
        NoteDisplay noteDisplay = noteService.updateNote(updateNote, NOTE_1_ID, USER_EMAIL);

        //then
        assertThat(noteDisplay).isNotNull();
        assertThat(noteDisplay.getText()).isEqualTo(NOTE_TEXT2);

        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    public void shouldRemoveOneNote() {

        //given
        Note note = createOneNote(NOTE_1_ID, NOTE_TEXT1);
        notes.add(note);

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);
        when(noteRepository.findNoteById(NOTE_1_ID)).thenReturn(note);

        //when
        noteService.removeNote(NOTE_1_ID, USER_EMAIL);

        //then
        verify(noteRepository, times(1)).deleteNoteById(anyLong());
    }
}