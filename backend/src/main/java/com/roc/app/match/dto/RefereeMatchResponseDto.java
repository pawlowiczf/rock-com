package com.roc.app.match.dto;

import com.roc.app.match.MatchStatus;

import java.time.LocalDateTime;

public record RefereeMatchResponseDto(
        Integer matchId,
        Integer competitionId,
        LocalDateTime matchDate,
        MatchStatus status,
        String player1Name,
        String player2Name,
        String score
) {}
