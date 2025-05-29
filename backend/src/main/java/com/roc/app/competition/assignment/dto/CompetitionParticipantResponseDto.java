package com.roc.app.competition.assignment.dto;

import com.roc.app.competition.assignment.CompetitionParticipant;
import com.roc.app.user.participant.ParticipantStatus;
import com.roc.app.user.participant.dto.ParticipantResponseDto;

import java.time.LocalDateTime;

public record CompetitionParticipantResponseDto(
        Integer competitionId,
        ParticipantResponseDto participant,
        ParticipantStatus participantStatus,
        LocalDateTime statusChangedAt
) {
    public static CompetitionParticipantResponseDto fromModel(CompetitionParticipant model, ParticipantResponseDto dto) {
        return new CompetitionParticipantResponseDto(
                model.getCompetitionId(),
                dto,
                model.getParticipantStatus(),
                model.getStatusChangeDate()
        );
    }
}