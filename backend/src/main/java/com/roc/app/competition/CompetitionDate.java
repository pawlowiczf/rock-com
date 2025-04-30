package com.roc.app.competition;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = CompetitionDate.FieldNames.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CompetitionDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FieldNames.DATE_ID)
    private Integer dateId;

    @ManyToOne
    @JoinColumn(name = FieldNames.COMPETITION_ID, nullable = false)
    private Competition competition;

    @Column(name = FieldNames.START_TIME)
    private LocalDateTime startTime;

    @Column(name = FieldNames.END_TIME)
    private LocalDateTime endTime;

    public static final class FieldNames {
        public static final String TABLE_NAME = "competition_dates";
        public static final String DATE_ID = "date_id";
        public static final String COMPETITION_ID = "competition_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
    }
}
