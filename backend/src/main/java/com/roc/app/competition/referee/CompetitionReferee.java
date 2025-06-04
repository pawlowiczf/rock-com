package com.roc.app.competition.referee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = CompetitionReferee.FieldNames.TABLE_NAME)
@IdClass(CompetitionReferee.CompetitionRefereeId.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionReferee {

    @Id
    @Column(name = FieldNames.COMPETITION_ID)
    private Integer competitionId;

    @Id
    @Column(name = FieldNames.REFEREE_ID)
    private Integer refereeId;

    public static final class FieldNames {
        public static final String TABLE_NAME = "competition_referees";
        public static final String COMPETITION_ID = "competition_id";
        public static final String REFEREE_ID = "referee_id";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetitionRefereeId implements Serializable {
        private Integer competitionId;
        private Integer refereeId;
    }
}


