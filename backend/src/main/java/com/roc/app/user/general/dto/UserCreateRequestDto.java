package com.roc.app.user.general.dto;

import com.roc.app.user.general.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequestDto(
        @NotBlank @Max(50)
        String firstName,
        @NotBlank @Max(50)
        String lastName,
        @NotBlank @Max(50) @Email
        String email,
        @NotBlank @Max(50)
        String city,
        @NotBlank @Max(12)
        String phoneNumber
) {
    public User toModel() {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .city(city)
                .phoneNumber(phoneNumber)
                .build();
    }
}
