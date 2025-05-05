package com.roc.app.competition;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = CompetitionDate.FieldNames.TABLE_NAME)
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

    public CompetitionDate() {}

    // Getters and Setters

    public Integer getDateId() {
        return dateId;
    }

    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompetitionDate that)) return false;
        return Objects.equals(dateId, that.dateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateId);
    }

    public static final class FieldNames {
        public static final String TABLE_NAME = "competition_dates";
        public static final String DATE_ID = "date_id";
        public static final String COMPETITION_ID = "competition_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
    }
}
