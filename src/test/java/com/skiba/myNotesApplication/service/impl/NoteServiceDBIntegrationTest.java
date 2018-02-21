package com.skiba.myNotesApplication.service.impl;

import com.skiba.myNotesApplication.api.dto.NoteCreation;
import com.skiba.myNotesApplication.api.dto.NoteDisplay;
import com.skiba.myNotesApplication.api.service.NoteService;
import com.skiba.myNotesApplication.data.TestDataLoader;
import com.skiba.myNotesApplication.model.Note;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.NoteRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.service.mapper.NoteCreationToNoteMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class NoteServiceDBIntegrationTest {


    @Autowired
    UserRepository userRepository;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    NoteCreationToNoteMapper noteCreationToNoteMapper;

    @Before
    public void initializeUserAndNoteSet() {
        User user = userRepository.findUserByEmail(TestDataLoader.USER_EMAIL);


        Note firstNote = new Note();
        firstNote.setUser(user);
        firstNote.setText("buy a christmas tree");
        noteRepository.save(firstNote);
    }


    @Test
    public void shouldAddOneNoteForUser() {

        //given
        String noteText = "note text";
        NoteCreation noteCreation = new NoteCreation(noteText);


        //when
        final NoteDisplay note1 = noteService.addNoteForUser(noteCreation, TestDataLoader.USER_EMAIL);


        //then
        assertThat(note1).isNotNull();
        assertThat(note1.getId()).isNotNull();
        assertThat(note1.getText()).isEqualTo(noteText);

    }


    @Test
    public void shouldAddNoteWithBigText()
    {
        //given
        String bigText = "0123456789";
        StringBuilder builder = new StringBuilder(bigText);
        for(int i =0; i<30; i++)
        {
            builder.append(bigText);
        }
        bigText = builder.toString();
        Note note = new Note();
        note.setText(bigText);

        //when
        final Note save = noteRepository.save(note);

        //then
        assertThat(save.getText()).isEqualTo(bigText);

    }

}