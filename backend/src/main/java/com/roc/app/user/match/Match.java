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

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Integer getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Integer player1Id) {
        this.player1Id = player1Id;
    }

    public Integer getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Integer player2Id) {
        this.player2Id = player2Id;
    }

    public Integer getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(Integer refereeId) {
        this.refereeId = refereeId;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }
}
