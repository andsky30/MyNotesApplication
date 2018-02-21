package com.skiba.myNotesApplication.api.controller;

import com.skiba.myNotesApplication.api.dto.BirthdayInfoCreation;
import com.skiba.myNotesApplication.api.dto.BirthdayDisplay;
import com.skiba.myNotesApplication.model.BirthdayInfo;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.BirthdayRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.api.service.BirthdayService;
import com.skiba.myNotesApplication.validation.ApiError;
import org.apache.tomcat.util.codec.binary.Base64;
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

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static com.skiba.myNotesApplication.api.controller.BirthdayController.MESSAGE_AFTER_BIRTHDAY_DELETION;
import static com.skiba.myNotesApplication.api.dto.BirthdayInfoCreation.BIRTHDAY_PERSON_SIZE_MESSAGE;
import static com.skiba.myNotesApplication.validation.BirthdayInfoNotFoundException.BIRTHDAY_NOT_FOUND_MESSAGE;
import static com.skiba.myNotesApplication.validation.validators.DateFormat.DATE_FORMAT_VALIDATOR_MESSAGE;
import static com.skiba.myNotesApplication.validation.validators.PastStringDate.PAST_DATE_VALIDATOR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BirthdayControllerTest {

    private static final String BIRTHDAY_PERSON = "Marek Nowak";
    private static final int EXPECTED_AGE = 25;
    private static final int SET_SIZE_1 = 1;
    private static final String BIRTHDAY_PERSON2 = "Michał Lewandowski";
    private static final int NOT_EXISTING_BIRTHDAY_ID = 156;
    private static final String TOO_LONG_BIRTHDAY_PERSON_NAME = "This birthdayPerson has more than 40 characters";
    private static final String USER_PASSWORD = "pass";
    private static final String USER_EMAIL = "email@test.pjn";
    private static final String WRONG_PASSWORD = "asdasdasd";
    public static final String BAD_CREDENTIALS_MESSAGE = "Bad credentials";
    public static final String NOT_EMPTY_MESSAGE = "may not be empty";

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BirthdayService birthdayService;
    @Autowired
    BirthdayRepository birthdayRepository;

    private User user;

    @Before
    public void setUp() {

        user = new User();
        user.setFirstName("Billy");
        user.setLastName("Thornton");
        user.setPassword(USER_PASSWORD);
        user.setEmail(USER_EMAIL);

        testRestTemplate.getRestTemplate()
                .getInterceptors()
                .add(new BasicAuthorizationInterceptor(USER_EMAIL, USER_PASSWORD));
    }

    @After
    public void clearUserAndTestrestemplate() {
        userRepository.delete(user);
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }


    @Test
    public void shouldReturnAllBirthdaysForUser() {

        //given
        Set<BirthdayInfo> birthdayInfoSet = new HashSet<>();

        LocalDate dateOfBirth = LocalDate.of(1992, Month.JUNE, 2);
        BirthdayInfo birthdayInfo = createBirthdayInfo(BIRTHDAY_PERSON, dateOfBirth);
        birthdayInfoSet.add(birthdayInfo);

        user.setBirthdayInfos(birthdayInfoSet);

        userRepository.save(user);

        //when
        ResponseEntity<Set<BirthdayDisplay>> responseEntity = testRestTemplate
                .exchange("/api/birthdays",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Set<BirthdayDisplay>>() {
                        });

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check birthdayDisplay set
        Set<BirthdayDisplay> returnedBirthdaySet = responseEntity.getBody();
        assertThat(returnedBirthdaySet).isNotNull();
        assertThat(returnedBirthdaySet).hasSize(SET_SIZE_1);

        //check single birthdayDisplay
        BirthdayDisplay returnedSingleBirthday = returnedBirthdaySet.iterator().next();
        assertThat(returnedSingleBirthday.getBirthDayPerson()).isEqualTo(BIRTHDAY_PERSON);
        assertThat(returnedSingleBirthday.getAge()).isEqualTo(EXPECTED_AGE);
        assertThat(returnedSingleBirthday.getDaysLeftUntilBirthday()).isBetween(1, 365);

    }

    private BirthdayInfo createBirthdayInfo(String birthdayPerson, LocalDate dateOfBirth) {
        BirthdayInfo birthdayInfo1 = new BirthdayInfo();
        birthdayInfo1.setBirthdayPerson(birthdayPerson);
        birthdayInfo1.setDateOfBirth(dateOfBirth);
        return birthdayInfo1;
    }

    @Test
    public void shouldReturnUnauthorizedStatusWhenUserEmailIsInvalid() {

        //given
        userRepository.save(user);

        testRestTemplate.getRestTemplate()
                .getInterceptors()
                .clear();

        testRestTemplate.getRestTemplate()
                .getInterceptors()
                .add(new BasicAuthorizationInterceptor(USER_EMAIL, WRONG_PASSWORD));

        //when
        ResponseEntity<Error> responseEntity = testRestTemplate
                .exchange("/api/birthdays",
                        HttpMethod.GET,
                        null,
                        Error.class);

        //than
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody().getMessage()).isEqualTo(BAD_CREDENTIALS_MESSAGE);
    }

    @Test
    public void shouldReturnSingleBirthdayById() {

        //given
        Set<BirthdayInfo> birthdayInfoSet = new HashSet<>();

        LocalDate dateOfBirth = LocalDate.of(1992, Month.JUNE, 2);
        BirthdayInfo birthdayInfo1 = createBirthdayInfo(BIRTHDAY_PERSON, dateOfBirth);
        birthdayInfoSet.add(birthdayInfo1);

        user.setBirthdayInfos(birthdayInfoSet);

        userRepository.save(user);

        final Long birthdayInfo1Id = birthdayInfo1.getId();

        //when
        ResponseEntity<BirthdayDisplay> responseEntity = testRestTemplate
                .exchange("/api/birthdays/" + birthdayInfo1Id,
                        HttpMethod.GET,
                        null,
                        BirthdayDisplay.class);

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check single note
        BirthdayDisplay returnedBirthday = responseEntity.getBody();
        assertThat(returnedBirthday).isNotNull();

        assertThat(returnedBirthday.getBirthDayPerson()).isEqualTo(BIRTHDAY_PERSON);
        assertThat(returnedBirthday.getAge()).isGreaterThan(0);
        assertThat(returnedBirthday.getDaysLeftUntilBirthday()).isGreaterThan(0);
    }

    @Test
    public void shouldReturnNotFoundStatusWhenBirthdayIdIsInvalid() {

        //given
        userRepository.save(user);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .getForEntity("/api/birthdays/" + NOT_EXISTING_BIRTHDAY_ID,
                        ApiError.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo(String.format(BIRTHDAY_NOT_FOUND_MESSAGE, NOT_EXISTING_BIRTHDAY_ID));
    }


    @Test
    public void shouldReturnNotFoundStatusWhenBirthdayIdExistsButBelongsToAnotherUser() {

        //given
        Set<BirthdayInfo> birthdayInfoSet = new HashSet<>();

        LocalDate dateOfBirth = LocalDate.of(1992, Month.JUNE, 2);
        BirthdayInfo birthdayInfo1 = createBirthdayInfo(BIRTHDAY_PERSON, dateOfBirth);
        birthdayInfoSet.add(birthdayInfo1);

        user.setBirthdayInfos(birthdayInfoSet);

        userRepository.save(user);

        //second user (unauthorized) with his birthdayInfo
        User user2 = new User();
        user2.setFirstName("Billy");
        user2.setLastName("Thornton");
        user2.setPassword("pass2");
        user2.setEmail("plpl@plpl.pl");

        Set<BirthdayInfo> birthdayInfoSet2 = new HashSet<>();

        LocalDate dateOfBirth2 = LocalDate.of(2005, Month.JUNE, 4);
        BirthdayInfo birthdayInfo2 = createBirthdayInfo("Asdasdasd", dateOfBirth2);

        birthdayInfoSet2.add(birthdayInfo2);
        user2.setBirthdayInfos(birthdayInfoSet2);

        userRepository.save(user2);

        Long birthdayOfUnauthorizedUserId = birthdayInfo2.getId();

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .getForEntity("/api/birthdays/" + birthdayOfUnauthorizedUserId,
                        ApiError.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo(String.format(BIRTHDAY_NOT_FOUND_MESSAGE, birthdayOfUnauthorizedUserId));
    }

    @Test
    public void shouldAddOneBirthdayForUser() {

        //given
        userRepository.save(user);

        String date = "2000-01-12";
        BirthdayInfoCreation birthdayInfoToAdd = new BirthdayInfoCreation(BIRTHDAY_PERSON, date);

        HttpEntity<BirthdayInfoCreation> requestEntity = new HttpEntity<>(birthdayInfoToAdd);

        //when
        ResponseEntity<BirthdayDisplay> responseEntity = testRestTemplate
                .exchange("/api/birthdays",
                        HttpMethod.POST,
                        requestEntity,
                        BirthdayDisplay.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        BirthdayDisplay returnedBirthdayInfoFromPostMethod = responseEntity.getBody();
        assertThat(returnedBirthdayInfoFromPostMethod.getBirthDayPerson()).isEqualTo(BIRTHDAY_PERSON);
    }

    //not used
    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    @Test
    public void shouldNotAllowToAddBirthdayInfoWithoutCompleteData() {

        //given
        userRepository.save(user);

        String date = "2000-01-12";
        BirthdayInfoCreation birthdayInfoToAdd = new BirthdayInfoCreation();
        birthdayInfoToAdd.setDateOfBirth(date);

        HttpEntity<BirthdayInfoCreation> requestEntity = new HttpEntity<>(birthdayInfoToAdd);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/birthdays",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(NOT_EMPTY_MESSAGE);

    }

    @Test
    public void shouldNotAllowToAddBirthdayInfoWithTooLongPerson() {

        //given
        userRepository.save(user);

        String date = "2000-01-02";
        BirthdayInfoCreation birthdayInfoToAdd = new BirthdayInfoCreation();
        birthdayInfoToAdd.setDateOfBirth(date);
        birthdayInfoToAdd.setBirthdayPerson(TOO_LONG_BIRTHDAY_PERSON_NAME);

        HttpEntity<BirthdayInfoCreation> requestEntity = new HttpEntity<>(birthdayInfoToAdd);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/birthdays",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(BIRTHDAY_PERSON_SIZE_MESSAGE);
    }

    @Test
    public void shouldNotAllowToAddBirthdayInfoWithWrongDateFormat() {

        //given
        userRepository.save(user);

        String date = "2000/12/02";
        BirthdayInfoCreation birthdayInfoToAdd = new BirthdayInfoCreation();
        birthdayInfoToAdd.setDateOfBirth(date);
        birthdayInfoToAdd.setBirthdayPerson(BIRTHDAY_PERSON2);

        HttpEntity<BirthdayInfoCreation> requestEntity = new HttpEntity<>(birthdayInfoToAdd);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/birthdays",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(DATE_FORMAT_VALIDATOR_MESSAGE);
    }

    @Test
    public void shouldNotAllowToAddBirthdayInfoWithDateFromTheFuture() {

        //given
        userRepository.save(user);

        String date = "2022-01-02";
        BirthdayInfoCreation birthdayInfoToAdd = new BirthdayInfoCreation();
        birthdayInfoToAdd.setDateOfBirth(date);
        birthdayInfoToAdd.setBirthdayPerson(BIRTHDAY_PERSON2);

        HttpEntity<BirthdayInfoCreation> requestEntity = new HttpEntity<>(birthdayInfoToAdd);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/birthdays",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).contains(PAST_DATE_VALIDATOR_MESSAGE);
    }

    @Test
    public void shouldUpdatePersonInBirthdayInfo() {

        //given
        Set<BirthdayInfo> birthdayInfos = new HashSet<>();

        LocalDate dateOfBirth = LocalDate.of(1992, Month.JUNE, 2);
        BirthdayInfo birthdayInfo1 = createBirthdayInfo(BIRTHDAY_PERSON, dateOfBirth);
        birthdayInfos.add(birthdayInfo1);

        user.setBirthdayInfos(birthdayInfos);
        userRepository.save(user);

        final Long birthdayInfoId = birthdayInfo1.getId();

        String date = "2000-01-12";
        BirthdayInfoCreation updatedInfo = new BirthdayInfoCreation(BIRTHDAY_PERSON2, date);

        HttpEntity<BirthdayInfoCreation> requestEntity = new HttpEntity<>(updatedInfo);

        //when
        ResponseEntity<BirthdayDisplay> responseEntity = testRestTemplate
                .exchange("/api/birthdays/" + birthdayInfoId,
                        HttpMethod.PUT,
                        requestEntity,
                        BirthdayDisplay.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        BirthdayDisplay returnedBirthdayInfoFromPutMethod = responseEntity.getBody();
        assertThat(returnedBirthdayInfoFromPutMethod.getBirthDayPerson()).isEqualTo(BIRTHDAY_PERSON2);
    }

    @Test
    public void shouldRemoveOneBirthdayInfo() {

        //given
        Set<BirthdayInfo> birthdayInfos = new HashSet<>();

        LocalDate dateOfBirth = LocalDate.of(1992, Month.JUNE, 6);
        BirthdayInfo birthdayInfo = createBirthdayInfo(BIRTHDAY_PERSON, dateOfBirth);
        birthdayInfos.add(birthdayInfo);

        user.setBirthdayInfos(birthdayInfos);
        userRepository.save(user);

        final Long birthdayInfoId = birthdayInfo.getId();

        //when
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/birthdays/" + birthdayInfoId,
                        HttpMethod.DELETE,
                        null,
                        String.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody())
                .isEqualTo(String.format(MESSAGE_AFTER_BIRTHDAY_DELETION, birthdayInfoId));

    }
}