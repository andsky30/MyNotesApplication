package com.skiba.myNotesApplication.api.dto;

import com.skiba.myNotesApplication.api.dto.validationOrder.First;
import com.skiba.myNotesApplication.api.dto.validationOrder.Second;
import com.skiba.myNotesApplication.api.dto.validationOrder.Third;
import com.skiba.myNotesApplication.validation.validators.DateFormat;
import com.skiba.myNotesApplication.validation.validators.PastStringDate;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.GroupSequence;
import javax.validation.constraints.Size;

@GroupSequence({ First.class, Second.class, Third.class, BirthdayInfoCreation.class })
public class BirthdayInfoCreation {

    public static final String BIRTHDAY_PERSON_SIZE_MESSAGE = "Birthday Person should have max 40 characters!";
    public static final String BIRTHDAY_DATE_NOT_EMPTY_MESSAGE = "Date of birth field cannot be empty!";


//    @NotBlank(message = "Birthday Person field cannot be empty!")
//    @NotEmpty(message = "Dfsdf") //dzialaja z hibernate a z constrains nie :<
    @Size(max = 40, message = BIRTHDAY_PERSON_SIZE_MESSAGE)
    @NotBlank
    private String birthdayPerson;

    @NotEmpty(groups = First.class, message = BIRTHDAY_DATE_NOT_EMPTY_MESSAGE)
    @DateFormat(groups = Second.class)
    @PastStringDate(groups = Second.class)
    private String dateOfBirth;

    public BirthdayInfoCreation() {
    }

    public BirthdayInfoCreation(String birthdayPerson, String dateOfBirth) {
        this.birthdayPerson = birthdayPerson;
        this.dateOfBirth = dateOfBirth;
    }

    public String getBirthdayPerson() {
        return birthdayPerson;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setBirthdayPerson(String birthdayPerson) {
        this.birthdayPerson = birthdayPerson;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BirthdayInfoCreation that = (BirthdayInfoCreation) o;

        if (birthdayPerson != null ? !birthdayPerson.equals(that.birthdayPerson) : that.birthdayPerson != null)
            return false;
        return dateOfBirth != null ? dateOfBirth.equals(that.dateOfBirth) : that.dateOfBirth == null;
    }

    @Override
    public int hashCode() {
        int result = birthdayPerson != null ? birthdayPerson.hashCode() : 0;
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BirthdayInfoCreation{" +
                "birthdayPerson='" + birthdayPerson + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
}
