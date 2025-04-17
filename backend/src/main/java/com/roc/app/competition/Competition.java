package com.roc.app.competition;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = Competition.FieldNames.TABLE_NAME)
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FieldNames.ID)
    private Integer competitionId;

    @Column(name = FieldNames.TYPE_ID, nullable = false)
    private Integer typeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FieldNames.TYPE_ID, insertable = false, updatable = false)
    private CompetitionType type;

    @Column(name = FieldNames.MATCH_DURATION, nullable = false)
    private Integer matchDurationMinutes;

    @Column(name = FieldNames.COURTS, nullable = false)
    private Integer availableCourts;

    @Column(name = FieldNames.PARTICIPANTS_LIMIT)
    private Integer participantsLimit;

    @Column(name = FieldNames.STREET, nullable = false)
    private String streetAddress;

    @Column(name = FieldNames.CITY, nullable = false)
    private String city;

    @Column(name = FieldNames.POSTAL_CODE, nullable = false)
    private String postalCode;

    @Column(name = FieldNames.REGISTRATION_OPEN, nullable = false)
    private Boolean registrationOpen;

    public Competition() {}

    // Getters & Setters

    public Integer getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId) {
        this.competitionId = competitionId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getMatchDurationMinutes() {
        return matchDurationMinutes;
    }

    public void setMatchDurationMinutes(Integer matchDurationMinutes) {
        this.matchDurationMinutes = matchDurationMinutes;
    }

    public Integer getAvailableCourts() {
        return availableCourts;
    }

    public void setAvailableCourts(Integer availableCourts) {
        this.availableCourts = availableCourts;
    }

    public Integer getParticipantsLimit() {
        return participantsLimit;
    }

    public void setParticipantsLimit(Integer participantsLimit) {
        this.participantsLimit = participantsLimit;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getRegistrationOpen() {
        return registrationOpen;
    }

    public void setRegistrationOpen(Boolean registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    // equals & hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Competition)) return false;
        Competition that = (Competition) o;
        return Objects.equals(competitionId, that.competitionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(competitionId);
    }

    public static final class FieldNames {
        public static final String TABLE_NAME = "competitions";
        public static final String ID = "competition_id";
        public static final String TYPE_ID = "type_id";
        public static final String MATCH_DURATION = "match_duration_minutes";
        public static final String COURTS = "available_courts";
        public static final String PARTICIPANTS_LIMIT = "participants_limit";
        public static final String STREET = "street_address";
        public static final String CITY = "city";
        public static final String POSTAL_CODE = "postal_code";
        public static final String REGISTRATION_OPEN = "registration_open";
    }
}