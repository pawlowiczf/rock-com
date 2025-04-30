package com.roc.app.competition.dto;

import com.roc.app.competition.CompetitionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompetitionDTO(

        Integer competitionId,

        @NotNull(message = "Competition type is required")
        @Enumerated(EnumType.STRING)
        CompetitionType type,

        @NotNull(message = "Match duration is required")
        @Min(value = 1, message = "Match duration must be at least 1 minute")
        Integer matchDurationMinutes,

        @NotNull(message = "Available courts is required")
        @Min(value = 1, message = "At least one court must be available")
        Integer availableCourts,

        @Min(value = 2, message = "Participants limit must be at least 2")
        Integer participantsLimit,

        @NotBlank(message = "Street address is required")
        @Size(max = 255, message = "Street address must not exceed 255 characters")
        String streetAddress,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @NotBlank(message = "Postal code is required")
        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        Boolean registrationOpen

) {}
