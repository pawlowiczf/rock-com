package com.roc.app.user.general.dto;

public record UserUpdateRequestDto (
        String firstName,
        String lastName,
        String email,
        String city,
        String phoneNumber
) { }
