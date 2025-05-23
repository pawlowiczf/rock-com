package com.roc.app.match.dto;

import com.roc.app.match.MatchStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ParticipantMatchResponseDto(

        @NotNull(message = "Match ID is required")
        Integer matchId,

        @NotNull(message = "Competition ID is required")
        Integer competitionId,

        @NotNull(message = "Match date is required")
        LocalDateTime matchDate,

        @NotBlank(message = "Status is required")
        MatchStatus status,

        String opponentName,

        String score,

        Boolean isWinner,

        String refereeName
) {}
