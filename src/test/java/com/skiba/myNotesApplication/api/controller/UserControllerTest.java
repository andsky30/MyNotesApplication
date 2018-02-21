package com.skiba.myNotesApplication.api.controller;

import com.google.common.collect.Sets;
import com.skiba.myNotesApplication.api.dto.UserCreation;
import com.skiba.myNotesApplication.api.dto.UserDisplay;
import com.skiba.myNotesApplication.data.TestDataLoader;
import com.skiba.myNotesApplication.model.Role;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.RoleRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.validation.ApiError;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.Set;

import static com.skiba.myNotesApplication.api.controller.BirthdayControllerTest.BAD_CREDENTIALS_MESSAGE;
import static com.skiba.myNotesApplication.api.controller.UserController.MESSAGE_AFTER_USER_BY_EMAIL_DELETION;
import static com.skiba.myNotesApplication.api.controller.UserController.MESSAGE_AFTER_USER_BY_ID_DELETION;
import static com.skiba.myNotesApplication.api.dto.UserCreation.*;
import static com.skiba.myNotesApplication.data.TestDataLoader.*;
import static com.skiba.myNotesApplication.validation.validators.HasNumber.HAS_NUMBER_MESSAGE;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    private static final long USER_ID_2 = 2L;
    private static final String EMAIL_USER = "ads@asd.pl";
    private static final String EMAIL_ADMIN = "adm@admin.pl";
    private static final String PASSWORD_USER = "asdasd3asdasd";
    private static final String PASSWORD_ADMIN = "admin34";
    private static final String FIRST_NAME_USER = "Peter";
    private static final String FIRST_NAME_ADMIN = "Lucy";
    private static final String LAST_NAME_USER = "Griffin";
    private static final String LAST_NAME_ADMIN = "Griffin";
    private static final long NOT_EXISTING_USER_ID = 1230L;
    private static final int SET_SIZE_1 = 1;
    private static final int SET_SIZE_2 = 2;
    private static final String WRONG_PASSWORD = "sasdfsdfsdf";
    private static final String FORBIDDEN_MESSAGE = "Dostęp zabroniony";
    private static final String EMAIL3 = "qweqwe@aqwe.pl";
    private static final String PASSWORD3 = "qwe123";
    private static final String FIRST_NAME3 = "Mark";
    private static final String LAST_NAME3 = "Johnson";
    public static final String TOO_SHORT_PASSWORD = "pass";

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TestDataLoader testDataLoader;



    @Before
    public void setUp() {
      //  testDataLoader.createAdminUser();

       /* user = createUser(EMAIL_USER,
                PASSWORD_USER, FIRST_NAME_USER, LAST_NAME_USER, USER_ROLE);
        userRepository.save(user);

        admin = createUser(EMAIL_ADMIN,
                PASSWORD_ADMIN, FIRST_NAME_ADMIN, LAST_NAME_ADMIN, ADMIN_ROLE);
        userRepository.save(admin);*/

    }


    @After
    public void clearUserAndTestrestemplate() {

        //testDataLoader.removeAdmin();
//        roleRepository.deleteAll();
       // userRepository.delete(user);
       // userRepository.delete(admin);
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void shouldReturnAllUsers() {

        //given
        addBasicAuthorizationInterceptorAsAdmin();


        //when
        ResponseEntity<Set<UserDisplay>> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Set<UserDisplay>>() {
                        });

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check users set
        Set<UserDisplay> returnedUsersSet = responseEntity.getBody();
        assertThat(returnedUsersSet).isNotNull();
        assertThat(returnedUsersSet).hasSize(SET_SIZE_2);

        //check single user
        Optional<UserDisplay> returnedAdminUserDisplayOptional = returnedUsersSet
                .stream()
                .filter(nd -> nd.getEmail() == EMAIL_ADMIN)
                .findFirst();
        returnedAdminUserDisplayOptional.ifPresent(userDisplay -> {
            Assertions.assertThat(userDisplay.getFirstName()).isEqualTo(FIRST_NAME_ADMIN);
        });
    }

    @Test
    public void shouldReturnForbiddenStatusWhenUserIsNotAdmin() {

        //given

        testRestTemplate.getRestTemplate().getInterceptors().clear();

        addBasicAuthorizationInterceptorAsUser();
        //when
        ResponseEntity<Error> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.GET,
                        null,
                        Error.class);

        //than
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Assertions.assertThat(responseEntity.getBody().getMessage()).isEqualTo(FORBIDDEN_MESSAGE);
    }

    @Test
    public void shouldReturnUnauthorizedStatusWhenUserEmailIsInvalid() {

        //given
        testRestTemplate.getRestTemplate()
                .getInterceptors()
                .add(new BasicAuthorizationInterceptor(EMAIL_ADMIN, WRONG_PASSWORD));
        //when
        ResponseEntity<Error> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.GET,
                        null,
                        Error.class);

        //than
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertThat(responseEntity.getBody().getMessage()).isEqualTo(BAD_CREDENTIALS_MESSAGE);
    }

    @Test
    public void shouldReturnSingleUserByIdForUser() {

        //given
        Long userId = userRepository.findUserByEmail(USER_EMAIL).getId();

        addBasicAuthorizationInterceptorAsAdmin();

        //when
        ResponseEntity<UserDisplay> responseEntity = testRestTemplate
                .exchange("/api/users/" + userId,
                        HttpMethod.GET,
                        null,
                        UserDisplay.class);

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check single note
        UserDisplay returnedUser = responseEntity.getBody();
        assertThat(returnedUser).isNotNull();

        assertThat(returnedUser.getEmail()).isEqualTo(USER_EMAIL);
        assertThat(returnedUser.getFirstName()).isEqualTo(USER_FIRST_NAME);
    }

    @Test
    public void shouldReturnSingleUserDisplayForUser() {

        //given
        addBasicAuthorizationInterceptorAsUser();

        //when
        ResponseEntity<UserDisplay> responseEntity = testRestTemplate
                .exchange("/api/users/account",
                        HttpMethod.GET,
                        null,
                        UserDisplay.class);

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check single note
        UserDisplay returnedUser = responseEntity.getBody();
        assertThat(returnedUser).isNotNull();

        assertThat(returnedUser.getEmail()).isEqualTo(USER_EMAIL);
        assertThat(returnedUser.getFirstName()).isEqualTo(USER_FIRST_NAME);
    }

    @Test
    public void shouldNotAllowUserToGetAnotherById() {

        //given

        final Long adminId = userRepository.findUserByEmail(EMAIL_ADMIN).getId();

        addBasicAuthorizationInterceptorAsUser();

        //when
        ResponseEntity<Error> responseEntity = testRestTemplate
                .exchange("/api/users/" + adminId,
                        HttpMethod.GET,
                        null,
                        Error.class);

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        //check single note
       // assertThat(responseEntity.getBody().getMessage()).isEqualTo(FORBIDDEN_MESSAGE);
    }

    @Test
    public void shouldAddOneUser() {

        //given
        UserCreation userCreation = new UserCreation(
                EMAIL3, PASSWORD3, FIRST_NAME3, LAST_NAME3);

        HttpEntity<UserCreation> requestEntity = new HttpEntity<>(userCreation);

        //when
        ResponseEntity<UserDisplay> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.POST,
                        requestEntity,
                        UserDisplay.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        UserDisplay userDisplay = responseEntity.getBody();
        assertThat(userDisplay.getEmail()).isEqualTo(EMAIL3);
        assertThat(userDisplay.getId()).isGreaterThan(0);

        User userByEmail = userRepository.findUserByEmail(EMAIL3);
        userRepository.delete(userByEmail);
    }

    @Test
    public void shouldNotAllowToAddUserWithoutCompleteData_Password() {

        //given

        UserCreation userCreation = new UserCreation();
        userCreation.setEmail(EMAIL3);
        userCreation.setFirstName(FIRST_NAME3);
        userCreation.setLastName(LAST_NAME3);

        HttpEntity<UserCreation> requestEntity = new HttpEntity<>(userCreation);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(PASSWORD_NOT_EMPTY_MESSAGE);
    }

    @Test
    public void shouldNotAllowToAddUserWithoutCompleteData_FLastName() {

        //given
        UserCreation userCreation = new UserCreation();
        userCreation.setEmail(EMAIL3);
        userCreation.setPassword(PASSWORD3);
        userCreation.setFirstName(FIRST_NAME3);

        HttpEntity<UserCreation> requestEntity = new HttpEntity<>(userCreation);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(LAST_NAME_NOT_BLANK_MESSAGE);
    }

    @Test
    public void shouldNotAllowToAddUserWithWrongPasswordFormat_WithoutNumber() {

        //given
        UserCreation userCreation = new UserCreation();
        userCreation.setEmail(EMAIL3);
        userCreation.setPassword("password without number");
        userCreation.setFirstName(FIRST_NAME3);
        userCreation.setLastName(LAST_NAME3);

        HttpEntity<UserCreation> requestEntity = new HttpEntity<>(userCreation);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(HAS_NUMBER_MESSAGE);
    }

    @Test
    public void shouldNotAllowToAddUserWithWrongPasswordFormat_TooShort() {

        //given
        UserCreation userCreation = new UserCreation();
        userCreation.setEmail(EMAIL3);
        userCreation.setPassword(TOO_SHORT_PASSWORD);
        userCreation.setFirstName(FIRST_NAME3);
        userCreation.setLastName(LAST_NAME3);

        HttpEntity<UserCreation> requestEntity = new HttpEntity<>(userCreation);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(PASSWORD_LENGTH_MESSAGE);
    }

    @Test
    public void shouldNotAllowToAddUserWithWrongEmailFormat() {

        //given
        UserCreation userCreation = new UserCreation();
        userCreation.setEmail("bad-email-format.pl");
        userCreation.setPassword(PASSWORD3);
        userCreation.setFirstName(FIRST_NAME3);
        userCreation.setLastName(LAST_NAME3);

        HttpEntity<UserCreation> requestEntity = new HttpEntity<>(userCreation);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(EMAIL_FORMAT_MESSAGE);
    }

    @Test
    @Ignore
    public void shouldRemoveOneUserById() {

        //given
        addBasicAuthorizationInterceptorAsAdmin();

        final Long userId = userRepository.findUserByEmail(USER_EMAIL).getId();

        //when
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/users/" + userId,
                        HttpMethod.DELETE,
                        null,
                        String.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody())
                .isEqualTo(String.format(MESSAGE_AFTER_USER_BY_ID_DELETION, userId));

    }

    @Test
    @Ignore
    public void shouldRemoveOneUserByEmail() {

        //given
        addBasicAuthorizationInterceptorAsUser();

        //when
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/users",
                        HttpMethod.DELETE,
                        null,
                        String.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody())
                .isEqualTo(String.format(MESSAGE_AFTER_USER_BY_EMAIL_DELETION, USER_EMAIL));

    }

    private void addBasicAuthorizationInterceptorAsUser() {
        testRestTemplate.getRestTemplate()
                .getInterceptors()
                .add(new BasicAuthorizationInterceptor(EMAIL_USER, PASSWORD_USER));
    }

    private void addBasicAuthorizationInterceptorAsAdmin() {
        testRestTemplate.getRestTemplate()
                .getInterceptors()
                .add(new BasicAuthorizationInterceptor(EMAIL_ADMIN, PASSWORD_ADMIN));
    }

    private User createUser(String email, String password,
                            String firstName, String lastName, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(Sets.newHashSet(role));
        return user;
    }

}