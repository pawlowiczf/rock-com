package com.roc.app.competition.dto;

import com.roc.app.competition.CompetitionDate;

import java.time.LocalDateTime;

public record CompetitionDateResponseDto (
        Integer dateId,
        Integer competitionId,
        LocalDateTime startTime,
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
