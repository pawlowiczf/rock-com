package com.roc.app.match.dto;

import com.roc.app.match.MatchStatus;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record MatchUpdateRequestDto(
        Integer player1Id,
        Integer player2Id,
        Integer refereeId,
        @Future(message = "Match date must be in the future") LocalDateTime matchDate,
        MatchStatus status,
        String score,
        Integer winnerId
) {}