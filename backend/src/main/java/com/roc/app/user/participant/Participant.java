package com.roc.app.user.participant;

import com.roc.app.user.general.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @Column(name = FieldNames.ID, nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = FieldNames.ID, nullable = false)
    private User userDetails;

    @Column(name = FieldNames.BIRTH_DATE, nullable = false)
    private LocalDate birthDate;

    public Participant() {}

    public Participant(User userDetails, LocalDate birthDate) {
        this.userDetails = userDetails;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User users) {
        this.userDetails = users;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public static final class FieldNames {
        public static final String ID = "user_id";
        public static final String BIRTH_DATE = "birth_date";
    }

}