package com.roc.app.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FieldNames.ID)
    private Integer userId;

    @Column(name = FieldNames.FIRST_NAME, nullable = false, length = 50)
    private String firstName;

    @Column(name = FieldNames.LAST_NAME, nullable = false, length = 50)
    private String lastName;

    @Column(name = FieldNames.EMAIL, nullable = false, length = 50, unique = true)
    private String email;

    public User() {
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, email);
    }

    public static final class FieldNames {
        public static final String ID = "user_id";
        public static final String FIRST_NAME = "firstname";
        public static final String LAST_NAME = "lastname";
        public static final String EMAIL = "email";
    }
}