package com.roc.app.user.general;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FieldNames.ID)
    private Long userId;

    @Column(name = FieldNames.FIRST_NAME, nullable = false, length = 50)
    private String firstName;

    @Column(name = FieldNames.LAST_NAME, nullable = false, length = 50)
    private String lastName;

    @Column(name = FieldNames.EMAIL, nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = FieldNames.CITY, nullable = false, length = 50)
    private String city;

    @Column(name = FieldNames.PHONE_NUMBER, nullable = false)
    private String phoneNumber;

    public User() {
    }

    public User(String firstName, String lastName, String email, String city, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.phoneNumber = phoneNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(city, user.city) &&
                Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, email, city, phoneNumber);
    }

    public static final class FieldNames {
        public static final String ID = "user_id";
        public static final String FIRST_NAME = "firstname";
        public static final String LAST_NAME = "lastname";
        public static final String EMAIL = "email";
        public static final String CITY = "city";
        public static final String PHONE_NUMBER = "phone_number";
    }
}