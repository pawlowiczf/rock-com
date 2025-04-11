package com.roc.app.user.dto;

public record UserResponseDto(
        Integer userId,
        String firstName,
        String lastName,
        String email
) {}
