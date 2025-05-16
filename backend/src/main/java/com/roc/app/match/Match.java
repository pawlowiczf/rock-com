package com.roc.app.match;

import com.roc.app.competition.Competition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = Match.FieldNames.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FieldNames.ID)
    private Integer matchId;

    @ManyToOne
    @JoinColumn(name = FieldNames.COMPETITION_ID, nullable = false)
    private Competition competition;

    @Column(name = FieldNames.PLAYER1_ID, nullable = false)
    private Integer player1Id;

    @Column(name = FieldNames.PLAYER2_ID, nullable = false)
    private Integer player2Id;

    @Column(name = FieldNames.REFEREE_ID)
    private Integer refereeId;

    @Column(name = FieldNames.MATCH_DATE, nullable = false)
    private LocalDateTime matchDate;

    @Enumerated(EnumType.STRING)
    @Column(name = FieldNames.STATUS, nullable = false)
    private MatchStatus status;

    @Column(name = FieldNames.SCORE)
    private String score;

    @Column(name = FieldNames.WINNER_ID)
    private Integer winnerId;

    public static final class FieldNames {
        public static final String TABLE_NAME = "matches";
        public static final String ID = "match_id";
        public static final String COMPETITION_ID = "competition_id";
        public static final String PLAYER1_ID = "player1_id";
        public static final String PLAYER2_ID = "player2_id";
        public static final String REFEREE_ID = "referee_id";
        public static final String MATCH_DATE = "match_date";
        public static final String STATUS = "status";
        public static final String SCORE = "score";
        public static final String WINNER_ID = "winner_id";
    }
}
