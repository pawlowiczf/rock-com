package com.roc.app.user.match;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "competition_participants")
@IdClass(CompetitionParticipant.CompetitionParticipantId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionParticipant {

    @Id
    @Column(name = "competition_id")
    private Integer competitionId;

    @Id
    @Column(name = "participant_id")
    private Integer participantId;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "status_change_date")
    private LocalDateTime statusChangeDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetitionParticipantId implements Serializable {
        private Integer competitionId;
        private Integer participantId;
    }
}
