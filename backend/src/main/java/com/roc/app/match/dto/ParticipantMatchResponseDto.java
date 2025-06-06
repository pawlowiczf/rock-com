package com.roc.app.match.dto;

import com.roc.app.match.MatchStatus;

import java.time.LocalDateTime;

public record ParticipantMatchResponseDto(
        String name,
        LocalDateTime matchDate,
        MatchStatus status,
        String opponent,
        String score,
        Boolean isWinner,
        String referee
) {}
