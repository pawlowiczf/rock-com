package com.roc.app.user.referee.general.dto;

import com.roc.app.user.general.User;
import com.roc.app.user.participant.Participant;
import com.roc.app.user.participant.dto.ParticipantResponseDto;
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
        User details = referee.getUserDetails();
        return new RefereeResponseDto(
                referee.getUserId(),
                details.getFirstName(),
                details.getLastName(),
                details.getEmail(),
                details.getCity(),
                details.getPhoneNumber()
        );
    }
}
