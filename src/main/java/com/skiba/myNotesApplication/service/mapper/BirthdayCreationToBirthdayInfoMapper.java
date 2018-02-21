package com.skiba.myNotesApplication.service.mapper;

import com.skiba.myNotesApplication.api.dto.BirthdayInfoCreation;
import com.skiba.myNotesApplication.model.BirthdayInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import static com.skiba.myNotesApplication.service.mapper.BirthdayInfoToBirthdayDisplayMapper.BIRTHDAY_DATE_FORMATTER;

@Component
public class BirthdayCreationToBirthdayInfoMapper {

    public BirthdayInfo map(BirthdayInfoCreation birthdayInfoCreation){
        BirthdayInfo birthdayInfo = new BirthdayInfo();
        birthdayInfo.setBirthdayPerson(birthdayInfoCreation.getBirthdayPerson());

        LocalDate date = LocalDate.parse(birthdayInfoCreation.getDateOfBirth(),
                BIRTHDAY_DATE_FORMATTER);
        birthdayInfo.setDateOfBirth(date);

        return birthdayInfo;
    }
}
