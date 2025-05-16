package com.roc.app.match;

import com.roc.app.competition.Competition;
import com.roc.app.match.dto.*;
import com.roc.app.match.exception.MatchNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<ParticipantMatchResponseDto> getParticipantMatches(Integer participantId) {
        return matchRepository.findParticipantMatches(participantId);
    }

    public List<RefereeMatchResponseDto> getRefereeMatches(Integer refereeId) {
        return matchRepository.findRefereeMatches(refereeId);
    }

    public Integer createMatch(MatchCreateRequestDto dto) {
        Match match = Match.builder()
                .competition(new Competition(dto.competitionId()))
                .player1Id(dto.player1Id())
                .player2Id(dto.player2Id())
                .refereeId(dto.refereeId())
                .matchDate(dto.matchDate())
                .status(dto.status())
                .build();

        return matchRepository.save(match).getMatchId();
    }


    public void updateMatch(Integer matchId, MatchUpdateRequestDto dto) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        if (dto.player1Id() != null) match.setPlayer1Id(dto.player1Id());
        if (dto.player2Id() != null) match.setPlayer2Id(dto.player2Id());
        if (dto.refereeId() != null) match.setRefereeId(dto.refereeId());
        if (dto.matchDate() != null) match.setMatchDate(dto.matchDate());
        if (dto.status() != null) match.setStatus(dto.status());
        if (dto.score() != null) match.setScore(dto.score());
        if (dto.winnerId() != null) match.setWinnerId(dto.winnerId());

        matchRepository.save(match);
    }

    public void deleteMatch(Integer matchId) {
        matchRepository.deleteById(matchId);
    }

}