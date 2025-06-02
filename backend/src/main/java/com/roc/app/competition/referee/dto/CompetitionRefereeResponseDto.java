package com.roc.app.competition.referee.dto;

import com.roc.app.competition.referee.CompetitionReferee;

public record CompetitionRefereeResponseDto(Integer competitionId, Long refereeId) {
        public static CompetitionRefereeResponseDto fromModel(CompetitionReferee model) {
            return new CompetitionRefereeResponseDto(
                    model.getCompetitionId(),
                    model.getRefereeId()
            );
        }
}
