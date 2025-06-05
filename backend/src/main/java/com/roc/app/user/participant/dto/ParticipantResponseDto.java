package com.roc.app.user.participant.dto;

import com.roc.app.user.participant.Participant;

import java.time.LocalDate;

public record ParticipantResponseDto(
        Integer userId,
        String firstName,
        String lastName,
        String email,
        String city,
        String phoneNumber,
        LocalDate birthDate
) {
    public static ParticipantResponseDto fromModel(Participant participant) {
        return new ParticipantResponseDto(
                participant.getUserId(),
                participant.getFirstName(),
                participant.getLastName(),
                participant.getEmail(),
                participant.getCity(),
                participant.getPhoneNumber(),
                participant.getBirthDate()
        );
    }
}
