package com.roc.app.user.referee.general.dto;

import com.roc.app.user.referee.general.Referee;

public record RefereeResponseDto(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String city,
        String phoneNumber
) {
    public static RefereeResponseDto fromModel(Referee referee) {
        return new RefereeResponseDto(
                referee.getUserId(),
                referee.getFirstName(),
                referee.getLastName(),
                referee.getEmail(),
                referee.getCity(),
                referee.getPhoneNumber()
        );
    }
}
