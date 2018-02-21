package com.skiba.myNotesApplication.service.impl;

import com.google.common.collect.Sets;
import com.skiba.myNotesApplication.api.dto.UserCreation;
import com.skiba.myNotesApplication.api.dto.UserDisplay;
import com.skiba.myNotesApplication.api.service.UserService;
import com.skiba.myNotesApplication.model.Role;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.RoleRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.service.mapper.UserCreationToUserMapper;
import com.skiba.myNotesApplication.service.mapper.UserToUserDisplayMapper;
import com.skiba.myNotesApplication.validation.UserByEmailNotFoundException;
import com.skiba.myNotesApplication.validation.UserByIdNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.skiba.myNotesApplication.data.DataLoader.ROLE_USER;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final long USER_ID_2 = 2L;
    private static final String EMAIL_1 = "ads@asd.pl";
    private static final String PASSWORD_1 = "asdasd3asdasd";
    private static final String FIRST_NAME_1 = "Peter";
    private static final String LAST_NAME_1 = "Griffin";
    private static final long NOT_EXISTING_USER_ID = 1230L;
    private static final String NOT_EXISTING_USER_MAIL = "notexisting@usermail.pl";
    private static final int SET_SIZE_2 = 2;

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;

    UserToUserDisplayMapper userToUserDisplayMapper = new UserToUserDisplayMapper();
    UserCreationToUserMapper userCreationToUserMapper = new UserCreationToUserMapper();

    @InjectMocks
    private UserService userService = new UserServiceImpl(userRepository, roleRepository,
            userToUserDisplayMapper, userCreationToUserMapper);

    private User user;
    private Set<User> users;

    @Before
    public void initializeUser() {
        user = new User();
        user.setId(USER_ID_2);
        user.setEmail(EMAIL_1);
        user.setPassword(PASSWORD_1);
        user.setFirstName(FIRST_NAME_1);
        user.setLastName(LAST_NAME_1);
        user.setRoles(Sets.newHashSet(new Role(ROLE_USER)));

        users = new HashSet<>();
        users.add(user);
    }

    @Test
    public void shouldReturnAllUsers() {

        //given
        when(userRepository.findAll()).thenReturn(new ArrayList<>(users));

        //when
        Set<UserDisplay> allUsers = userService.getAllUsers();

        //then
        assertThat(allUsers).hasSize(1);
        assertThat(allUsers.iterator().next().getFirstName()).isEqualTo(FIRST_NAME_1);
        assertThat(allUsers.iterator().next().getEmail()).isEqualTo(EMAIL_1);
    }

    @Test
    public void shouldReturnUserById() {

        //given
        when(userRepository.findUserById(USER_ID_2)).thenReturn(user);

        //when
        UserDisplay userDisplay = userService.getSingleUserDisplayById(USER_ID_2);

        //then
        assertThat(userDisplay.getFirstName()).isEqualTo(FIRST_NAME_1);
        assertThat(userDisplay.getLastName()).isEqualTo(LAST_NAME_1);
    }

    @Test
    public void shouldReturnUserByEmail() {

        //given
        when(userRepository.findUserByEmail(EMAIL_1)).thenReturn(user);

        //when
        UserDisplay userDisplay = userService.getSingleUserDisplayByEmail(EMAIL_1);

        //then
        assertThat(userDisplay.getFirstName()).isEqualTo(FIRST_NAME_1);
        assertThat(userDisplay.getLastName()).isEqualTo(LAST_NAME_1);
    }

    @Test(expected = UserByEmailNotFoundException.class)
    public void shouldNotAllowToInsertInvalidUserEmail() {
        //given

        //when
        userService.getSingleUserDisplayByEmail(NOT_EXISTING_USER_MAIL);
        //then
    }

    @Test(expected = UserByIdNotFoundException.class)
    public void shouldNotAllowToInsertInvalidUserId() {
        //given

        //when
        userService.getSingleUserDisplayById(NOT_EXISTING_USER_ID);
        //then
    }

    @Test
    public void shouldAddOneUser(){

        //given
        UserCreation userCreation = new UserCreation("abc@abc.pl","asda123", "Steve", "Ooo");

        //when
        UserDisplay userDisplay = userService.addUser(userCreation);

        //then
        assertThat(userDisplay).isNotNull();
        assertThat(users).hasSize(SET_SIZE_2);

        verify(userRepository, times(1)).save(any(User.class));
    }




}