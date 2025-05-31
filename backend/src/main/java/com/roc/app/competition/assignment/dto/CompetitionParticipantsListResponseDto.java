package com.roc.app.competition.assignment.dto;

import com.roc.app.user.participant.dto.ParticipantResponseDto;

import java.util.List;

public record CompetitionParticipantsListResponseDto(
        List<ParticipantResponseDto> confirmed,
        List<ParticipantResponseDto> waiting
) {
}
