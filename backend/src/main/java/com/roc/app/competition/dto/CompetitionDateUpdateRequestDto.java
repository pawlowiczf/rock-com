package com.roc.app.competition.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CompetitionDateUpdateRequestDto (
        @NotNull(message = "Start time is required")
        LocalDateTime startTime,
        @NotNull(message = "End time is required")
        LocalDateTime endTime
){ }
