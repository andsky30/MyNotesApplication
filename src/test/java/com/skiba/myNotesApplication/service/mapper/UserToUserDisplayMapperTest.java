package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.UserDisplay;
import com.skiba.myNotesApplication.model.User;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class UserToUserDisplayMapperTest {

    private static final Long USER_ID = 5L;
    private static final String USER_EMAIL = "abc@abc.pl";
    private static final String USER_PASSWORD = "abc3abc";
    private static final String USER_FIRST_NAME = "Michał";
    private static final String USER_LAST_NAME = "Wołodyjowski";

    UserToUserDisplayMapper userToUserDisplayMapper = new UserToUserDisplayMapper();

    @Test
    public void shouldReturnUserDisplay(){

        //given
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setFirstName(USER_FIRST_NAME);
        user.setLastName(USER_LAST_NAME);

        //when
        UserDisplay userDisplay = userToUserDisplayMapper.map(user);

        //then
        assertThat(userDisplay).isNotNull();
        assertThat(userDisplay.getLastName()).isEqualTo(USER_LAST_NAME);
        assertThat(userDisplay.getEmail()).isEqualTo(USER_EMAIL);
        assertThat(userDisplay.getId()).isEqualTo(USER_ID);
    }

}