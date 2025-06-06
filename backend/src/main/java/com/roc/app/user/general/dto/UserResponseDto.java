package com.roc.app.user.general.dto;

import com.roc.app.user.general.User;

public record UserResponseDto (
        Integer userId,
        String firstName,
        String lastName,
        String email,
        String city,
        String phoneNumber
){
    public static UserResponseDto fromModel (User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCity(),
                user.getPhoneNumber()
        );
    }
}
