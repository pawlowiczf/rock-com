package com.roc.app.match;

import com.roc.app.competition.Competition;
import com.roc.app.competition.CompetitionRepository;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import com.roc.app.match.dto.*;
import com.roc.app.match.exception.MatchNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final CompetitionRepository competitionRepository;

    public MatchService(MatchRepository matchRepository, CompetitionRepository competitionRepository) {
        this.matchRepository = matchRepository;
        this.competitionRepository = competitionRepository;
    }

    public List<ParticipantMatchResponseDto> getParticipantMatches(Integer participantId) {
        return matchRepository.findParticipantMatches(participantId);
    }

    public List<RefereeMatchResponseDto> getRefereeMatches(Integer refereeId) {
        return matchRepository.findRefereeMatches(refereeId);
    }

    public Integer createMatch(MatchCreateRequestDto dto) {
        Competition competition = competitionRepository.findById(dto.competitionId())
                .orElseThrow(() -> new CompetitionNotFoundException(dto.competitionId()));

        Match match = dto.toModel(competition);
        return matchRepository.save(match).getMatchId();
    }

    public Integer createByeMatch(Competition competition, Integer playerId){
        Match match = Match.builder()
                .competition(competition)
                .player1Id(playerId)
                .status(MatchStatus.BYE)
                .winnerId(playerId)
                .build();
        return matchRepository.save(match).getMatchId();
    }

    public Integer createScheduledMatch(Competition competition, Integer player1Id, Integer player2Id, Integer refereeId, LocalDateTime matchDate){
        Match match = Match.builder()
                .competition(competition)
                .player1Id(player1Id)
                .player2Id(player2Id)
                .refereeId(null)
                .matchDate(matchDate)
                .status(MatchStatus.SCHEDULED)
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