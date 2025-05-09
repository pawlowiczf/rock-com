package com.roc.app.user.participant.dto;

import com.roc.app.user.general.dto.UserCreateRequestDto;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ParticipantCreateRequestDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank @Email
        String email,
        @NotBlank
        String city,
        @NotBlank

        String phoneNumber,
        @Past @NotNull
        LocalDate birthDate
) {
    public UserCreateRequestDto toUserCreateRequestDto() {
        return new UserCreateRequestDto(firstName, lastName, email, city, phoneNumber);
    }
}
