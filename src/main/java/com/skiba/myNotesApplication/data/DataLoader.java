package com.skiba.myNotesApplication.data;

import com.google.common.collect.Sets;
import com.skiba.myNotesApplication.model.*;
import com.skiba.myNotesApplication.repository.RoleRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;

@Component
@Profile("dev")
@Transactional
public class DataLoader implements ApplicationRunner {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    public final static Role USER_ROLE = new Role(ROLE_USER) ;
    public final static Role ADMIN_ROLE = new Role(ROLE_ADMIN);

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public void run(ApplicationArguments args) {

        roleRepository.save(USER_ROLE);
        roleRepository.save(ADMIN_ROLE);

        User firstUser = new User();
        firstUser.setFirstName("Billy");
        firstUser.setLastName("Thornton");
        firstUser.setPassword("pass11");
        firstUser.setEmail("email@test.pjn");

        firstUser.setRoles(Sets.newHashSet(ADMIN_ROLE));

        Note note = new Note();
        note.setUser(firstUser);
        note.setText("Przykladowy text");
        firstUser.setNotes(Sets.newHashSet(note));

        BirthdayInfo birthdayInfo = new BirthdayInfo();
        birthdayInfo.setDateOfBirth(LocalDate.of(1992, Month.JUNE, 2));
        birthdayInfo.setBirthdayPerson("Andrew S");
        firstUser.setBirthdayInfos(Sets.newHashSet(birthdayInfo));

        userRepository.save(firstUser);

        User secondUser = new User();
        secondUser.setFirstName("Andy");
        secondUser.setLastName("Pachitto");
        secondUser.setPassword("and123");
        secondUser.setEmail("and@and.pl");

        secondUser.setRoles(Sets.newHashSet(USER_ROLE));

        userRepository.save(secondUser);

        User thirdUser = new User();
        thirdUser.setFirstName("Andgy");
        thirdUser.setLastName("Pacghitto");
        thirdUser.setPassword("andg123");
        thirdUser.setEmail("agnd@angd.pl");

        thirdUser.setRoles(Sets.newHashSet(USER_ROLE));

        userRepository.save(thirdUser);
    }
}