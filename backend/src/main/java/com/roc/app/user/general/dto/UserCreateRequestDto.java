package com.roc.app.user.general.dto;

import com.roc.app.user.general.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDto(
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
