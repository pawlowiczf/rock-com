package com.roc.app.competition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = Competition.FieldNames.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FieldNames.ID)
    private Integer competitionId;

    @Column(name = FieldNames.NAME, nullable = false)
    private String competitionName;

    @Enumerated(EnumType.STRING)
    @Column(name = FieldNames.TYPE, nullable = false)
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

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionDate> competitionDates = new ArrayList<>();

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

    // Needed for matchService
    public Competition(Integer competitionId) {
        this.competitionId = competitionId;
    }

}


