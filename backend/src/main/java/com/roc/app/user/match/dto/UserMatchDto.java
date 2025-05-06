package com.roc.app.user.match.dto;

import java.time.LocalDateTime;

public class UserMatchDto {
    private Integer matchId;
    private Integer competitionId;
    private LocalDateTime matchDate;
    private String status;
    private String opponentName;
    private String score;
    private Boolean isWinner;

    public UserMatchDto(Integer matchId, Integer competitionId, LocalDateTime matchDate, String status,
                        String opponentName, String score, Boolean isWinner) {
        this.matchId = matchId;
        this.competitionId = competitionId;
        this.matchDate = matchDate;
        this.status = status;
        this.opponentName = opponentName;
        this.score = score;
        this.isWinner = isWinner;
    }

    // Getters and Setters
}
