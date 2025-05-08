package com.roc.app.user.general;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements UserDetails {
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

    @Column(name = FieldNames.PASSWORD, nullable = false, length = 60)
    private String password;

    @Column(name = FieldNames.CITY, nullable = false, length = 50)
    private String city;

    @Column(name = FieldNames.PHONE_NUMBER, nullable = false)
    private String phoneNumber;

    @Override
    public String getUsername() {
        return email;
    }

    public static final class FieldNames {
        public static final String ID = "user_id";
        public static final String FIRST_NAME = "firstname";
        public static final String LAST_NAME = "lastname";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String CITY = "city";
        public static final String PHONE_NUMBER = "phone_number";
    }
}