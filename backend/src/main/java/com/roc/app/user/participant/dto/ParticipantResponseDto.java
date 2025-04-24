package com.roc.app.user.participant.dto;

import com.roc.app.user.general.User;
import com.roc.app.user.participant.Participant;

import java.time.LocalDate;

public record ParticipantResponseDto(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String city,
        String phoneNumber,
        LocalDate birthDate
) {
    public static ParticipantResponseDto fromModel(Participant participant) {
        User details = participant.getUserDetails();
        return new ParticipantResponseDto(
                participant.getId(),
                details.getFirstName(),
                details.getLastName(),
                details.getEmail(),
                details.getCity(),
                details.getPhoneNumber(),
                participant.getBirthDate()
        );
    }
}
