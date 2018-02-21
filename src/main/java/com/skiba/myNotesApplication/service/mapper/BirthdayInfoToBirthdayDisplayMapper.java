package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.BirthdayDisplay;
import com.skiba.myNotesApplication.model.BirthdayInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class BirthdayInfoToBirthdayDisplayMapper {


    public static final DateTimeFormatter BIRTHDAY_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final String BIRTHDAY_DATE_PATTERN = "yyyy-MM-dd";


    public BirthdayDisplay map(BirthdayInfo birthdayInfo) {

        Long id = birthdayInfo.getId();
        String birthdayPerson = birthdayInfo.getBirthdayPerson();
        String dateOfBirth = birthdayInfo.getDateOfBirth().format(BIRTHDAY_DATE_FORMATTER);

        LocalDate actualDate = LocalDate.now();
        int age = (int) birthdayInfo.getDateOfBirth().until(actualDate, ChronoUnit.YEARS);

        LocalDate nextBirthday = birthdayInfo.getDateOfBirth().plusYears(age + 1);
        int daysLeftUntilNextBirthday = (int) actualDate.until(nextBirthday, ChronoUnit.DAYS);

        return new BirthdayDisplay(id, age, daysLeftUntilNextBirthday, birthdayPerson, dateOfBirth);
    }
}
