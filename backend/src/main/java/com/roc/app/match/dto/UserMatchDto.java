package com.roc.app.match.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UserMatchDto(

        @NotNull(message = "Match ID is required")
        Integer matchId,

        @NotNull(message = "Competition ID is required")
        Integer competitionId,

        @NotNull(message = "Match date is required")
        LocalDateTime matchDate,

        @NotBlank(message = "Status is required")
        String status,

        @NotBlank(message = "Opponent name is required")
        String opponentName,

        @NotBlank(message = "Score is required")
        String score,

        @NotNull(message = "Winner status is required")
        Boolean isWinner

) {}
