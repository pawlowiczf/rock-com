package com.roc.app.match.dto;

import com.roc.app.competition.dto.CompetitionResponseDto;
import com.roc.app.match.Match;
import com.roc.app.match.MatchStatus;

public record MatchResponseDto(
        Integer matchId,
        Integer competitionId,
        Integer player1Id,
        Integer player2Id,
        Integer refereeId,
        String score,
        Integer winnerId,
        MatchStatus status
) {
    public static MatchResponseDto fromModel(Match match) {
        return new MatchResponseDto(
                match.getMatchId(),
                match.getCompetition().getCompetitionId(),
                match.getPlayer1Id(),
                match.getPlayer2Id(),
                match.getRefereeId(),
                match.getScore(),
                match.getWinnerId(),
                match.getStatus()
        );
    }
}
