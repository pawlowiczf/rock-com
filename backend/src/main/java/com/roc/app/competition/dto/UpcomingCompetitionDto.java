package com.roc.app.competition.dto;

import com.roc.app.competition.CompetitionType;

import java.time.LocalDateTime;

public record UpcomingCompetitionDto(
        Integer competitionId,
        String name,
        CompetitionType type,
        String city,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean registrationOpen
) {}