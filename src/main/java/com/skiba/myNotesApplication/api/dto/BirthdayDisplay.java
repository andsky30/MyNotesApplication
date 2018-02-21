package com.skiba.myNotesApplication.api.dto;

public class BirthdayDisplay {

    private Long id;
    private int age; //
    private int daysLeftUntilBirthday; //
    private String birthDayPerson;
    private String dateOfBirth;


    private BirthdayDisplay() {
    }


    public BirthdayDisplay(Long id, int age, int daysLeftUntilBirthday, String birthDayPerson, String dateOfBirth) {
        this.id = id;
        this.age = age;
        this.daysLeftUntilBirthday = daysLeftUntilBirthday;
        this.birthDayPerson = birthDayPerson;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getDaysLeftUntilBirthday() {
        return daysLeftUntilBirthday;
    }

    public String getBirthDayPerson() {
        return birthDayPerson;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BirthdayDisplay that = (BirthdayDisplay) o;

        if (age != that.age) return false;
        if (daysLeftUntilBirthday != that.daysLeftUntilBirthday) return false;
        if (!id.equals(that.id)) return false;
        if (!birthDayPerson.equals(that.birthDayPerson)) return false;
        return dateOfBirth.equals(that.dateOfBirth);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + age;
        result = 31 * result + daysLeftUntilBirthday;
        result = 31 * result + birthDayPerson.hashCode();
        result = 31 * result + dateOfBirth.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BirthdayDisplay{" +
                "id=" + id +
                ", age=" + age +
                ", daysLeftUntilBirthday=" + daysLeftUntilBirthday +
                ", birthDayPerson='" + birthDayPerson + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
}
