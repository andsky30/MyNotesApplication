package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.BirthdayDisplay;
import com.skiba.myNotesApplication.model.BirthdayInfo;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static com.skiba.myNotesApplication.service.mapper.BirthdayInfoToBirthdayDisplayMapper.BIRTHDAY_DATE_FORMATTER;
import static org.assertj.core.api.Java6Assertions.assertThat;


public class BirthdayInfoToBirthdayDisplayMapperTest {

    private static final String BIRTHDAY_PERSON = "Mariusz";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1992, Month.JUNE,2);
    private static final int AGE = 25;

    BirthdayInfoToBirthdayDisplayMapper birthdayInfoToBirthdayDisplayMapper
            = new BirthdayInfoToBirthdayDisplayMapper();

    @Test
    public void shouldReturnBirthdayReminder(){

        //given
        String date1 = DATE_OF_BIRTH.format(BIRTHDAY_DATE_FORMATTER);

        BirthdayInfo birthdayInfo = new BirthdayInfo();
        birthdayInfo.setBirthdayPerson(BIRTHDAY_PERSON);
        birthdayInfo.setDateOfBirth(DATE_OF_BIRTH);

        //when
        BirthdayDisplay birthdayDisplay = birthdayInfoToBirthdayDisplayMapper
                .map(birthdayInfo);

        //then
        assertThat(birthdayDisplay).isNotNull();
        assertThat(birthdayDisplay.getBirthDayPerson()).isEqualTo(BIRTHDAY_PERSON);
        assertThat(birthdayDisplay.getDateOfBirth()).isEqualTo(date1);
        assertThat(birthdayDisplay.getAge()).isEqualTo(AGE);
        assertThat(birthdayDisplay.getDaysLeftUntilBirthday()).isGreaterThan(0);

        System.out.println(birthdayDisplay);
    }

}