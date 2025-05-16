package com.roc.app.match.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MatchCreateRequestDto(
        @NotNull Integer competitionId,
        @NotNull Integer player1Id,
        @NotNull Integer player2Id,
        @NotNull Integer refereeId,
        @NotNull @Future(message = "Match date must be in the future") LocalDateTime matchDate,
        @NotNull String status,
        String score,
        Integer winnerId
) {}
