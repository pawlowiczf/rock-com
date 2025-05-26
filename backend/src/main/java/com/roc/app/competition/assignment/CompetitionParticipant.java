package com.roc.app.competition.assignment;

import com.roc.app.user.participant.ParticipantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = CompetitionParticipant.FieldNames.TABLE_NAME)
@IdClass(CompetitionParticipant.CompetitionParticipantId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionParticipant {

    @Id
    @Column(name = FieldNames.COMPETITION_ID)
    private Integer competitionId;

    @Id
    @Column(name = FieldNames.PARTICIPANT_ID)
    private Long participantId;

    @Enumerated(EnumType.STRING)
    @Column(name = FieldNames.PARTICIPANT_STATUS, nullable = false)
    private ParticipantStatus participantStatus;

    @Column(name = FieldNames.STATUS_CHANGE_DATE)
    private LocalDateTime statusChangeDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetitionParticipantId implements Serializable {
        private Integer competitionId;
        private Long participantId;
    }

    public static final class FieldNames {
        public static final String TABLE_NAME = "competition_participants";
        public static final String COMPETITION_ID = "competition_id";
        public static final String PARTICIPANT_ID = "participant_id";
        public static final String PARTICIPANT_STATUS = "status";
        public static final String STATUS_CHANGE_DATE = "status_change_date";
    }
}
