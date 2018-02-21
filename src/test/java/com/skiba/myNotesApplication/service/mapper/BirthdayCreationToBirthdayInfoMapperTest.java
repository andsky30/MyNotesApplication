package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.BirthdayInfoCreation;
import com.skiba.myNotesApplication.model.BirthdayInfo;
import org.junit.Test;

import java.time.LocalDate;

import static com.skiba.myNotesApplication.service.mapper.BirthdayInfoToBirthdayDisplayMapper.BIRTHDAY_DATE_FORMATTER;
import static org.assertj.core.api.Assertions.assertThat;

public class BirthdayCreationToBirthdayInfoMapperTest {

    private static final String BIRTHDAY_PERSON = "Kamil";
    private static final String DATE_OF_BIRTH = "2000-01-05";

    BirthdayCreationToBirthdayInfoMapper birthdayCreationToBirthdayInfoMapper
            = new BirthdayCreationToBirthdayInfoMapper();

    @Test
    public void shouldReturnBirthdayInfo() {

        //given
        LocalDate localDate = LocalDate.parse(DATE_OF_BIRTH, BIRTHDAY_DATE_FORMATTER);

        BirthdayInfoCreation birthdayInfoCreation
                = new BirthdayInfoCreation(BIRTHDAY_PERSON, DATE_OF_BIRTH);


        //when
        BirthdayInfo birthdayInfo = birthdayCreationToBirthdayInfoMapper
                .map(birthdayInfoCreation);

        //then
        assertThat(birthdayInfo).isNotNull();
        assertThat(birthdayInfo.getBirthdayPerson()).isEqualTo(BIRTHDAY_PERSON);
        assertThat(birthdayInfo.getDateOfBirth()).isEqualTo(localDate);

        System.out.println(birthdayInfo);
    }
}