package com.roc.app.competition.dto;

import java.time.LocalDateTime;

public record UpcomingCompetitionDto(
        Integer competitionId,
        String typeLabel,
        String city,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean registrationOpen
) {}