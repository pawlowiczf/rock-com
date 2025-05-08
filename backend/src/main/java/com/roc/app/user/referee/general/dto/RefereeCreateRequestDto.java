package com.roc.app.user.referee.general.dto;

import com.roc.app.user.general.dto.UserCreateRequestDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefereeCreateRequestDto(
        @NotBlank @Size(max = 50)
        String firstName,
        @NotBlank @Size(max = 50)
        String lastName,
        @NotBlank @Size(max = 50) @Email
        String email,
        @NotBlank @Size(max = 50)
        String city,
        @NotBlank @Size(max = 12)
        String phoneNumber
) {
    public UserCreateRequestDto toUserCreateRequestDto() {
        return new UserCreateRequestDto(firstName, lastName, email, city, phoneNumber);
    }
}
