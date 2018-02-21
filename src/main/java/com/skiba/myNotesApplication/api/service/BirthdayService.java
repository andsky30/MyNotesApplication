package com.skiba.myNotesApplication.api.service;

import com.skiba.myNotesApplication.api.dto.BirthdayDisplay;
import com.skiba.myNotesApplication.api.dto.BirthdayInfoCreation;

import java.util.Set;

public interface BirthdayService {

    Set<BirthdayDisplay> getAllBirthdays (String email);

    BirthdayDisplay addBirthdayForUser(BirthdayInfoCreation birthdayInfoCreation, String email);

    void removeBirthday(Long birthdayId, String email);

    BirthdayDisplay updateBirthday(BirthdayInfoCreation birthdayInfoCreation, Long birthdayId, String email);

    BirthdayDisplay getSingleBirthday(Long birthdayId, String email);
}
