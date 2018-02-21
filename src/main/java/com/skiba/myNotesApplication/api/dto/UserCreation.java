package com.skiba.myNotesApplication.api.dto;

import com.skiba.myNotesApplication.api.dto.validationOrder.First;
import com.skiba.myNotesApplication.api.dto.validationOrder.Second;
import com.skiba.myNotesApplication.api.dto.validationOrder.Third;
import com.skiba.myNotesApplication.validation.validators.HasNumber;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.GroupSequence;

@GroupSequence({ First.class, Second.class, Third.class, UserCreation.class })
public class UserCreation {

    public static final String EMAIL_NOT_BLANK_MESSAGE = "E-mail address field cannot be blank!";
    public static final String FIRST_NAME_NOT_BLANK_MESSAGE = "First name address field cannot be blank!";
    public static final String LAST_NAME_NOT_BLANK_MESSAGE = "Last name address field cannot be blank!";
    public static final String EMAIL_LENGTH_MESSAGE = "E-mail has to contain max 80 characters!";
    public static final String PASSWORD_LENGTH_MESSAGE = "Password has to be between 5 and 30 characters!";
    public static final String FIRST_NAME_LENGTH_MESSAGE = "First name has to contain max 30 characters!";
    public static final String LAST_NAME_LENGTH_MESSAGE = "Last name has to contain max 30 characters!";
    public static final String PASSWORD_NOT_EMPTY_MESSAGE = "Password field cannot be empty!";
    public static final String EMAIL_FORMAT_MESSAGE = "Enter the correct e-mail address!";

    @NotBlank(groups = First.class, message = EMAIL_NOT_BLANK_MESSAGE)
    @Length(groups = Second.class, max = 80, message = EMAIL_LENGTH_MESSAGE)
    @Email(groups = Third.class, message = EMAIL_FORMAT_MESSAGE)
    private String email;


    @NotEmpty(groups = First.class, message = PASSWORD_NOT_EMPTY_MESSAGE)
    @Length(groups = Second.class, min = 5, max = 30, message = PASSWORD_LENGTH_MESSAGE)
    @HasNumber(groups = Third.class)
    private String password;

    @NotBlank(groups = First.class, message = FIRST_NAME_NOT_BLANK_MESSAGE)
    @Length(groups = Second.class, max = 30, message = FIRST_NAME_LENGTH_MESSAGE)
    private String firstName;

    @NotBlank(groups = First.class, message = LAST_NAME_NOT_BLANK_MESSAGE)
    @Length(groups = Second.class, max = 30, message = LAST_NAME_LENGTH_MESSAGE)
    private String lastName;

    public UserCreation() {
    }

    public UserCreation(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCreation that = (UserCreation) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        return lastName != null ? lastName.equals(that.lastName) : that.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserCreation{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
