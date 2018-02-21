package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.UserCreation;
import com.skiba.myNotesApplication.model.User;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserCreationToUserMapperTest {

    private static final String USER_EMAIL = "abc@abc.pl";
    private static final String USER_PASSWORD = "abc3abc";
    private static final String USER_FIRST_NAME = "Michał";
    private static final String USER_LAST_NAME = "Wołodyjowski";

    UserCreationToUserMapper userCreationToUserMapper = new UserCreationToUserMapper();

    @Test
    public void shouldReturnUser() {

        //given
        UserCreation userCreation = new UserCreation(USER_EMAIL,
                USER_PASSWORD, USER_FIRST_NAME, USER_LAST_NAME);

        //when
        User user = userCreationToUserMapper.map(userCreation);

        //then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(USER_EMAIL);
        assertThat(user.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(user.getFirstName()).isEqualTo(USER_FIRST_NAME);
    }
}