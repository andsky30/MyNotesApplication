package com.skiba.myNotesApplication.api.controller;

import com.skiba.myNotesApplication.api.dto.NoteCreation;
import com.skiba.myNotesApplication.api.dto.NoteDisplay;
import com.skiba.myNotesApplication.api.service.NoteService;
import com.skiba.myNotesApplication.data.TestDataLoader;
import com.skiba.myNotesApplication.model.Note;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.NoteRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.validation.ApiError;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static com.skiba.myNotesApplication.api.controller.BirthdayControllerTest.BAD_CREDENTIALS_MESSAGE;
import static com.skiba.myNotesApplication.api.controller.NoteController.MESSAGE_AFTER_NOTE_DELETION;
import static com.skiba.myNotesApplication.api.dto.NoteCreation.NOTE_TEXT_NOT_BLANK_MESSAGE;
import static com.skiba.myNotesApplication.api.dto.NoteCreation.NOTE_TEXT_SIZE_MESSAGE;

import static com.skiba.myNotesApplication.validation.NoteNotFoundException.NOTE_NOT_FOUND_MESSAGE;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NoteControllerTest {

    private static final int SIZE_OF_STRING_600 = 600;
    private static final String NOTE_TEXT1 = "Kolor zielony";
    private static final String NOTE_TEXT2 = "Kolor niebieski";
    private static final int SET_SIZE_1 = 1;
    private static final long NOT_EXISTING_NOTE_ID = 15550L;
    private static final String USER_PASSWORD = "pass11";
    private static final String USER_EMAIL = "email@test.pjn";
    private static final String BLANK_TEXT = "         ";
    private static final String WRONG_PASSWORD = "sasdfsdfsdf";

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NoteService noteService;
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    TestDataLoader testDataLoader;


    @Before
    public void setUp() {

        testRestTemplate.getRestTemplate()
                .getInterceptors()
                .add(new BasicAuthorizationInterceptor(USER_EMAIL, USER_PASSWORD));
    }


    @After
    public void clearUserAndTestrestemplate() {

        noteRepository.deleteAll();
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void shouldReturnAllNotesForUser() {

        //given
        addBasicNoteToUser(NOTE_TEXT2);

        //when
        ResponseEntity<Set<NoteDisplay>> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Set<NoteDisplay>>() {
                        });

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check notes set
        Set<NoteDisplay> returnedNotes = responseEntity.getBody();
        assertThat(returnedNotes).isNotNull();
        assertThat(returnedNotes).hasSize(SET_SIZE_1);

        //check single note
        NoteDisplay returnedNote = returnedNotes.iterator().next();
        assertThat(returnedNote.getText()).isEqualTo(NOTE_TEXT2);
    }


    private Note createNote(String noteText) {
        Note note1 = new Note();
        note1.setText(noteText);
        return note1;
    }

    @Test
    public void shouldReturnUnauthorizedStatusWhenUserEmailIsInvalid() {

        //given

        //when

        testRestTemplate.getRestTemplate()
                .getInterceptors()
                .clear();

        testRestTemplate.getRestTemplate()
                .getInterceptors()
                .add(new BasicAuthorizationInterceptor(USER_EMAIL, WRONG_PASSWORD));
        //when
        ResponseEntity<Error> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.GET,
                        null,
                        Error.class);

        //than
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(responseEntity.getBody().getMessage()).isEqualTo(BAD_CREDENTIALS_MESSAGE);
    }


    @Test
    public void shouldReturnSingleNoteById() {

        //given
        Note note = addBasicNoteToUser(NOTE_TEXT1);
        Long noteId = note.getId();

        //when
        ResponseEntity<NoteDisplay> responseEntity = testRestTemplate
                .exchange("/api/notes/" + noteId,
                        HttpMethod.GET,
                        null,
                        NoteDisplay.class);

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check single note
        NoteDisplay returnedNote = responseEntity.getBody();
        assertThat(returnedNote).isNotNull();

        assertThat(returnedNote.getText()).isEqualTo(NOTE_TEXT1);
    }

    @Test
    public void shouldReturnNotFoundStatusWhenNoteIdIsInvalid() {

        //given

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .getForEntity("/api/notes/" + NOT_EXISTING_NOTE_ID,
                        ApiError.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo(String.format(NOTE_NOT_FOUND_MESSAGE, NOT_EXISTING_NOTE_ID));
    }

    @Test// birthday?
    public void shouldReturnNotFoundStatusWhenBirthdayIdExistsButBelongsToAnotherUser() {

        //given
        addBasicNoteToUser(NOTE_TEXT1);

        //second user (unauthorized) with his birthdayInfo
        User user2 = new User();
        user2.setFirstName("Billy");
        user2.setLastName("Thornton");
        user2.setPassword("pass2");
        user2.setEmail("plpl@plpl.pl");

        Note note2 = createNote(NOTE_TEXT2);
        note2.setUser(user2);
        userRepository.save(user2);
        noteRepository.save(note2);
        Long noteOfUnauthorizedUserId = note2.getId();

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .getForEntity("/api/notes/" + noteOfUnauthorizedUserId,
                        ApiError.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo(String.format(NOTE_NOT_FOUND_MESSAGE, noteOfUnauthorizedUserId));
    }


    @Test
    public void shouldAddOneNoteForUser() {

        //given
        NoteCreation noteToAdd = new NoteCreation(NOTE_TEXT2);

        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(noteToAdd);

        //when
        ResponseEntity<NoteDisplay> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.POST,
                        requestEntity,
                        NoteDisplay.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        NoteDisplay returnedNoteFromPostMethod = responseEntity.getBody();
        assertThat(returnedNoteFromPostMethod.getText()).isEqualTo(NOTE_TEXT2);
    }


    @Test
    public void shouldNotAddNoteWithoutText() {

        //given
        NoteCreation noteToAdd = new NoteCreation();

        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(noteToAdd);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(NOTE_TEXT_NOT_BLANK_MESSAGE);
    }


    @Test
    public void shouldNotAddNoteWithBlankText() {

        //given
        NoteCreation noteToAdd = new NoteCreation(BLANK_TEXT);

        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(noteToAdd);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(NOTE_TEXT_NOT_BLANK_MESSAGE);
    }


    @Test
    public void shouldNotAddNoteWithTooLongText() {

        //given

        String noteTextWithMoreThan500Chars = generateLongString(SIZE_OF_STRING_600);

        NoteCreation noteToAdd = new NoteCreation(noteTextWithMoreThan500Chars);

        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(noteToAdd);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(noteToAdd.getText()).hasSize(SIZE_OF_STRING_600);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(NOTE_TEXT_SIZE_MESSAGE);

    }

    //method for generating String with n characters
    private String generateLongString(int numberOfCharacters) {
        StringBuilder outputString = new StringBuilder(numberOfCharacters);
        for (int i = 0; i < numberOfCharacters; i++) {
            outputString.append("a");
        }
        return outputString.toString();
    }

    @Test
    public void shouldUpdateNoteText() {

        //given
        Note note = addBasicNoteToUser(NOTE_TEXT1);
        Long noteId = note.getId();

        NoteCreation updatedNote = new NoteCreation(NOTE_TEXT2);

        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(updatedNote);

        //when
        ResponseEntity<NoteDisplay> responseEntity = testRestTemplate
                .exchange("/api/notes/" + noteId,
                        HttpMethod.PUT,
                        requestEntity,
                        NoteDisplay.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        NoteDisplay returnedNoteFromPutMethod = responseEntity.getBody();
        assertThat(returnedNoteFromPutMethod.getText()).isEqualTo(NOTE_TEXT2);
        assertThat(returnedNoteFromPutMethod.getId()).isNotNull();
    }

    @Test
    public void shouldRemoveOneNote() {

        //given
        Note note = addBasicNoteToUser(NOTE_TEXT2);
        Long noteId = note.getId();

        //when
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/notes/" + noteId,
                        HttpMethod.DELETE,
                        null,
                        String.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody())
                .isEqualTo(String.format(MESSAGE_AFTER_NOTE_DELETION, noteId));
    }

    public Note addBasicNoteToUser(String noteText) {
        return testDataLoader.addBasicNoteToUser(noteText);
    }


}