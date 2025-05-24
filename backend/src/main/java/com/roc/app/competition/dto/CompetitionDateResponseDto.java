package com.roc.app.competition.dto;

import com.roc.app.competition.CompetitionDate;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CompetitionDateResponseDto (
        @NotNull(message = "Competition date id is required")
        Integer dateId,
        @NotNull(message = "Competition id is required")
        Integer competitionId,
        @NotNull(message = "Start time is required")
        LocalDateTime startTime,
        @NotNull(message = "End time is required")
        LocalDateTime endTime
){
    public static CompetitionDateResponseDto fromModel(CompetitionDate competitionDate) {
        return new CompetitionDateResponseDto(
                competitionDate.getDateId(),
                competitionDate.getCompetition().getCompetitionId(),
                competitionDate.getStartTime(),
                competitionDate.getEndTime()
        );
    }
}
