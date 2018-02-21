package com.skiba.myNotesApplication.data;

import com.google.common.collect.Sets;
import com.skiba.myNotesApplication.model.Note;
import com.skiba.myNotesApplication.model.Role;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.NoteRepository;
import com.skiba.myNotesApplication.repository.RoleRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestDataLoader implements ApplicationRunner {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String USER_PASSWORD = "pass11";
    public static final String USER_EMAIL = "email@test.pjn";
    public static final String USER_FIRST_NAME = "Billy";
    public static final String USER_LAST_NAME = "Thornton";

    public static final String EMAIL_ADMIN = "adm@admin.pl";
    public static final String PASSWORD_ADMIN = "admin34";
    public static final String FIRST_NAME_ADMIN = "Lucy";
    public static final String LAST_NAME_ADMIN = "Griffin";


    private RoleRepository roleRepository;
    private UserRepository userRepository;

    private NoteRepository noteRepository;

    @Autowired
    public TestDataLoader(RoleRepository roleRepository, UserRepository userRepository, NoteRepository noteRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    public void run(ApplicationArguments args) {
        createAdmin();
        createUser();
    }

    private void createUser() {
        User firstUser = new User();
        firstUser.setFirstName(USER_FIRST_NAME);
        firstUser.setLastName(USER_LAST_NAME);
        firstUser.setPassword(USER_PASSWORD);
        firstUser.setEmail(USER_EMAIL);
        userRepository.save(firstUser);

        final Role userRole = roleRepository.findByName(ROLE_USER);

        firstUser.setRoles(Sets.newHashSet(userRole));
        userRepository.save(firstUser);
    }

    private void createAdmin() {
        User admin = new User();
        admin.setFirstName(FIRST_NAME_ADMIN);
        admin.setLastName(LAST_NAME_ADMIN);
        admin.setPassword(PASSWORD_ADMIN);
        admin.setEmail(EMAIL_ADMIN);
        userRepository.save(admin);


         Role userRole = new Role(ROLE_USER) ;
         Role adminRole = new Role(ROLE_ADMIN);
        admin.setRoles(Sets.newHashSet(userRole, adminRole));
        userRepository.save(admin);
    }

    public Note addBasicNoteToUser(String noteText) {
        Note note = new Note();
        note.setText(noteText);
        User userByEmail = userRepository.findUserByEmail(USER_EMAIL);
        note.setUser(userByEmail);
        noteRepository.save(note);
        return note;
    }




}