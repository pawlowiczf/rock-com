package com.roc.app.match.dto;

import com.roc.app.match.MatchStatus;

import java.time.LocalDateTime;

public record ParticipantMatchResponseDto(
        Integer matchId,
        Integer competitionId,
        LocalDateTime matchDate,
        MatchStatus status,
        String opponentName,
        String score,
        Boolean isWinner,
        String refereeName
) {}
