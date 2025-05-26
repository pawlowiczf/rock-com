package com.roc.app.competition.dto;

import com.roc.app.competition.CompetitionType;

public record CompetitionResponseDto(
        Integer competitionId,
        String name,
        CompetitionType type,
        Integer matchDurationMinutes,
        Integer availableCourts,
        Integer participantsLimit,
        String streetAddress,
        String city,
        String postalCode,
        Boolean registrationOpen
) {}
