package com.roc.app.user.participant.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ParticipantCreateRequestDto(
        @NotBlank @Max(50)
        String firstName,
        @NotBlank @Max(50)
        String lastName,
        @NotBlank @Max(50) @Email
        String email,
        @NotBlank @Max(50)
        String city,
        @NotBlank @Max(12)
        String phoneNumber,
        @Past @NotNull
        LocalDate birthDate
) {
}
