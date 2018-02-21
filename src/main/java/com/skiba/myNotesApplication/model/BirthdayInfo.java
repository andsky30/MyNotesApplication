package com.skiba.myNotesApplication.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import static com.skiba.myNotesApplication.service.mapper.BirthdayInfoToBirthdayDisplayMapper.BIRTHDAY_DATE_PATTERN;

@Entity
public class BirthdayInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String birthdayPerson;

    @DateTimeFormat(pattern = BIRTHDAY_DATE_PATTERN)
    private LocalDate dateOfBirth;

    public BirthdayInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBirthdayPerson() {
        return birthdayPerson;
    }

    public void setBirthdayPerson(String birthdayPerson) {
        this.birthdayPerson = birthdayPerson;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BirthdayInfo that = (BirthdayInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(birthdayPerson, that.birthdayPerson) &&
                Objects.equals(dateOfBirth, that.dateOfBirth);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, birthdayPerson, dateOfBirth);
    }

    @Override
    public String toString() {
        return "BirthdayInfo{" +
                "id=" + id +
                ", birthdayPerson='" + birthdayPerson + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
