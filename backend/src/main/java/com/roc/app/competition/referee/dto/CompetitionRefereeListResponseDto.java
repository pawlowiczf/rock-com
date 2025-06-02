package com.roc.app.competition.referee.dto;

import com.roc.app.competition.referee.CompetitionReferee;

import java.util.List;

public record CompetitionRefereeListResponseDto(
        List<CompetitionRefereeResponseDto> referees
) {
    public static CompetitionRefereeListResponseDto from(List<CompetitionRefereeResponseDto> list) {
        return new CompetitionRefereeListResponseDto(list);
    }
}
