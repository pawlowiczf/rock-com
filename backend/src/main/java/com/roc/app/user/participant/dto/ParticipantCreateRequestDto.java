package com.roc.app.user.participant.dto;

import com.roc.app.user.general.dto.UserCreateRequestDto;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ParticipantCreateRequestDto(
<<<<<<< HEAD
        @NotBlank @Size(max = 50)
        String firstName,
        @NotBlank @Size(max = 50)
        String lastName,
        @NotBlank @Size(max = 50) @Email
        String email,
        @NotBlank @Size(max = 50)
        String city,
        @NotBlank @Size(max = 12)
=======
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank @Email
        String email,
        @NotBlank
        String city,
        @NotBlank
>>>>>>> origin/main
        String phoneNumber,
        @Past @NotNull
        LocalDate birthDate
) {
    public UserCreateRequestDto toUserCreateRequestDto() {
        return new UserCreateRequestDto(firstName, lastName, email, city, phoneNumber);
    }
}
