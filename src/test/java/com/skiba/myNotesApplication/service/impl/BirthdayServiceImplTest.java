package com.skiba.myNotesApplication.service.impl;

import com.skiba.myNotesApplication.api.dto.BirthdayDisplay;
import com.skiba.myNotesApplication.api.dto.BirthdayInfoCreation;
import com.skiba.myNotesApplication.model.BirthdayInfo;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.BirthdayRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.api.service.BirthdayService;
import com.skiba.myNotesApplication.service.mapper.BirthdayCreationToBirthdayInfoMapper;
import com.skiba.myNotesApplication.service.mapper.BirthdayInfoToBirthdayDisplayMapper;
import com.skiba.myNotesApplication.validation.BirthdayInfoNotFoundException;
import com.skiba.myNotesApplication.validation.UserByEmailNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class BirthdayServiceImplTest {

    private static final Long BIRTHDAY_INFO_1_ID = 1L;
    private static final Long BIRTHDAY_INFO_2_ID = 2L;
    private static final Long NOT_EXISTING_BIRTHDAY_INFO_ID = 6546L;
    private static final String BIRTHDAY_PERSON1 = "Tomek";
    private static final String BIRTHDAY_PERSON2 = "Marek";
    private static final String BIRTHDAY_PERSON3 = "Mateusz";
    private static final int EXPECTED_AGE = 25;
    private static final int SET_SIZE_1 = 1;
    private static final int SET_SIZE_2 = 2;
    private static final int SET_SIZE_3 = 3;
    private static final String USER_EMAIL = "email@test.pjn";
    private static final String NOT_EXISTING_USER_MAIL = "notexisting@usermail.pl";


    @Mock
    private UserRepository userRepository;

    @Mock
    private BirthdayRepository birthdayRepository;

    private BirthdayInfoToBirthdayDisplayMapper birthdayInfoToBirthdayDisplayMapper
            = new BirthdayInfoToBirthdayDisplayMapper();
    private BirthdayCreationToBirthdayInfoMapper birthdayCreationToBirthdayInfoMapper
            = new BirthdayCreationToBirthdayInfoMapper();

    @InjectMocks
    private BirthdayService birthdayService
            = new BirthdayServiceImpl(birthdayRepository, userRepository,
            birthdayInfoToBirthdayDisplayMapper, birthdayCreationToBirthdayInfoMapper);

    private User user;
    private Long userId;
    private Set<BirthdayInfo> birthdayInfoSet;
    private BirthdayInfo birth1;
    private BirthdayInfo birth2;

    @Before
    public void initializeUserAndBirthdaySet() {
        user = new User();
        user.setFirstName("Billy");
        user.setLastName("Thornton");
        user.setPassword("pass");
        user.setEmail(USER_EMAIL);

        birthdayInfoSet = new HashSet<>();
        user.setBirthdayInfos(birthdayInfoSet);

        birth1 = new BirthdayInfo();
        birth1.setId(BIRTHDAY_INFO_1_ID);
        birth1.setBirthdayPerson(BIRTHDAY_PERSON1);
        birth1.setDateOfBirth(LocalDate.of(1992, Month.JUNE, 1));
        birthdayInfoSet.add(birth1);

        birth2 = new BirthdayInfo();
        birth2.setId(BIRTHDAY_INFO_2_ID);
        birth2.setBirthdayPerson(BIRTHDAY_PERSON2);
        birth2.setDateOfBirth(LocalDate.of(2000, Month.MARCH, 25));
        birthdayInfoSet.add(birth2);
    }

    @After
    public void makeUserNull() {
        user = null;
        userId = null;
        birthdayInfoSet = null;
        birth1 = null;
        birth2 = null;
    }

    @Test
    public void shouldReturnTwoBirthdayDisplays() {

        //given
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);

        //when
        Set<BirthdayDisplay> birthdayDisplaySet = birthdayService.getAllBirthdays(USER_EMAIL);

        //then
        assertThat(birthdayDisplaySet).hasSize(SET_SIZE_2);
        //assertions that birthdayDisplaySet contains calculated age and daysLeft
        Optional<BirthdayDisplay> returnedFirstBirthdayReminderOptional = birthdayDisplaySet
                .stream()
                .filter(br -> br.getId() == BIRTHDAY_INFO_1_ID)
                .findFirst();

        returnedFirstBirthdayReminderOptional.ifPresent(birthdayDisplay -> {
            assertThat(birthdayDisplay.getAge()).isEqualTo(EXPECTED_AGE);
            assertThat(birthdayDisplay.getDaysLeftUntilBirthday()).isNotNull();
        });

        birthdayDisplaySet.forEach(System.out::println);
    }


    @Test(expected = UserByEmailNotFoundException.class)
    public void shouldNotAllowToInsertInvalidUserEmail() {
        //given

        //when
        birthdayService.getAllBirthdays(NOT_EXISTING_USER_MAIL);

        //then
    }

    @Test
    public void shouldReturnSingleBirthdayDisplay() {

        //given
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);
        when(birthdayRepository.findBirthdayInfoById(BIRTHDAY_INFO_1_ID)).thenReturn(birth1);

        //when
        BirthdayDisplay birthdayDisplay = birthdayService.getSingleBirthday(BIRTHDAY_INFO_1_ID, USER_EMAIL);

        //then
        assertThat(birthdayDisplay).isNotNull();
        assertThat(birthdayDisplay.getId()).isEqualTo(BIRTHDAY_INFO_1_ID);
        assertThat(birthdayDisplay.getBirthDayPerson()).isEqualTo(BIRTHDAY_PERSON1);
        assertThat(birthdayDisplay.getAge()).isEqualTo(EXPECTED_AGE);
        assertThat(birthdayDisplay.getDaysLeftUntilBirthday()).isGreaterThan(0);
    }

    @Test(expected = BirthdayInfoNotFoundException.class)
    public void shouldNotAllowToInsertInvalidBirthdayId() {

        //given
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);
        //when
        birthdayService.getSingleBirthday(NOT_EXISTING_BIRTHDAY_INFO_ID, USER_EMAIL);
        //then
    }

    @Test(expected = UserByEmailNotFoundException.class)
    public void shouldNotAllowToInsertInvalidUserEmailForSingleBirthday() {

        //given

        //when
        birthdayService.getSingleBirthday(NOT_EXISTING_BIRTHDAY_INFO_ID, NOT_EXISTING_USER_MAIL);
        //then
    }

    @Test
    public void shouldAddBirthdayForUser() {

        //given
        String date = "2005-06-15";

        BirthdayInfoCreation birth3 = new BirthdayInfoCreation(BIRTHDAY_PERSON3, date);

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);

        //when
        BirthdayDisplay returnedBirthday = birthdayService.addBirthdayForUser(birth3, USER_EMAIL);

        //then
        assertThat(returnedBirthday).isNotNull();
        assertThat(birthdayInfoSet).hasSize(SET_SIZE_3);

        assertThat(returnedBirthday.getBirthDayPerson()).isEqualTo(BIRTHDAY_PERSON3);
        assertThat(returnedBirthday.getAge()).isGreaterThan(0);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateBirthday() {

        //given
        String date = "1992-01-14";

        BirthdayInfoCreation updatedBirthday = new BirthdayInfoCreation(BIRTHDAY_PERSON3, date);

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);
        when(birthdayRepository.findBirthdayInfoById(BIRTHDAY_INFO_1_ID)).thenReturn(birth1);
        when(birthdayRepository.save(birth1)).thenReturn(birth1);

        //when
        BirthdayDisplay birthdayDisplay = birthdayService.updateBirthday(updatedBirthday, BIRTHDAY_INFO_1_ID, USER_EMAIL);

        //then
        assertThat(birthdayDisplay).isNotNull();
        assertThat(birthdayDisplay.getBirthDayPerson()).isEqualTo(BIRTHDAY_PERSON3);

        verify(birthdayRepository, times(1)).save(any(BirthdayInfo.class));
    }

    @Test
    public void shouldRemoveOneBirthday() {

        //given
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(user);
        when(birthdayRepository.findBirthdayInfoById(BIRTHDAY_INFO_1_ID)).thenReturn(birth1);

        //when
        birthdayService.removeBirthday(BIRTHDAY_INFO_1_ID, USER_EMAIL);

        //then
        verify(birthdayRepository, times(1)).deleteBirthdayInfoById(anyLong());
    }


}