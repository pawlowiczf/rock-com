package com.roc.app.match.dto;

import com.roc.app.competition.Competition;
import com.roc.app.match.Match;

import java.time.LocalDateTime;

import com.roc.app.match.MatchStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record MatchCreateRequestDto(
        @NotNull Integer competitionId,
        @NotNull Integer player1Id,
        @NotNull Integer player2Id,
        @NotNull Integer refereeId,
        @NotNull @Future(message = "Match date must be in the future") LocalDateTime matchDate,
        @NotNull MatchStatus status,
        String score,
        Integer winnerId
) {
    public Match toModel(Competition competition) {
        return Match.builder()
                .competition(competition)
                .player1Id(player1Id)
                .player2Id(player2Id)
                .refereeId(refereeId)
                .matchDate(matchDate)
                .status(status)
                .score(score)
                .winnerId(winnerId)
                .build();
    }
}
