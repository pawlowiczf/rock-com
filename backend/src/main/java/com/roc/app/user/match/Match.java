package com.roc.app.user.match;

import com.roc.app.competition.Competition;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchId;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Column(name = "player1_id")
    private Integer player1Id;

    @Column(name = "player2_id")
    private Integer player2Id;

    @Column(name = "referee_id")
    private Integer refereeId;

    @Column(name = "match_date")
    private LocalDateTime matchDate;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private MatchStatus status;

    private String score;

    @Column(name = "winner_id")
    private Integer winnerId;

    // Getters and setters
}
