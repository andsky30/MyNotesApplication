package com.skiba.myNotesApplication.repository;

import com.skiba.myNotesApplication.model.BirthdayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BirthdayRepository extends JpaRepository<BirthdayInfo, Long> {

    BirthdayInfo findBirthdayInfoById(Long birthdayId);

    void deleteBirthdayInfoById(Long birthdayId);
}
