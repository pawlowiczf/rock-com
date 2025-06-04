package com.roc.app.match;

import com.roc.app.bracket.BracketRepository;
import com.roc.app.bracket.BracketRepository;
import com.roc.app.bracket.BracketService;
import com.roc.app.competition.Competition;
import com.roc.app.competition.CompetitionRepository;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import com.roc.app.match.dto.*;
import com.roc.app.match.exception.MatchNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final CompetitionRepository competitionRepository;
    private final BracketRepository bracketRepository;
    private final BracketService bracketService;

    public MatchService(MatchRepository matchRepository, CompetitionRepository competitionRepository, BracketRepository bracketRepository, BracketService bracketService) {
        this.matchRepository = matchRepository;
        this.competitionRepository = competitionRepository;
        this.bracketRepository = bracketRepository;
        this.bracketService = bracketService;
    }

    public MatchResponseDto getMatchById(Integer matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
        return MatchResponseDto.fromModel(match);
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

    public List<MatchResponseDto> getCompetitionMatchesByRefereeId(Integer competitionId, Integer refereeId) {
        return matchRepository.findByRefereeIdAndCompetitionCompetitionId(refereeId, competitionId)
                .stream()
                .map(MatchResponseDto::fromModel)
                .toList();
    }

    public Match createByeMatch(Competition competition, Integer playerId) {
        Match match = Match.builder()
                .competition(competition)
                .player1Id(playerId)
                .status(MatchStatus.BYE)
                .winnerId(playerId)
                .build();
        return matchRepository.save(match);
    }

    public Match createScheduledMatch(Competition competition, Integer player1Id, Integer player2Id, Integer refereeId, LocalDateTime matchDate) {
        Match match = Match.builder()
                .competition(competition)
                .player1Id(player1Id)
                .player2Id(player2Id)
                .refereeId(null)
                .matchDate(matchDate)
                .status(MatchStatus.SCHEDULED)
                .build();
        return matchRepository.save(match);
    }

    public void updateMatchesFollowingByeMatches(Integer competitionId) {
        List<Match> byeMatches = matchRepository.findByCompetition_CompetitionIdAndStatus(competitionId, MatchStatus.BYE);
        byeMatches.forEach(match -> {
            Integer newMatchId = bracketRepository.findNextMatchIdByMatchId(match.getMatchId())
                    .orElseThrow(() -> new MatchNotFoundException(match.getMatchId()));
            Match newMatch = matchRepository.findById(newMatchId)
                    .orElseThrow(() -> new MatchNotFoundException(newMatchId));
            if (newMatch.getPlayer1Id() == null) {
                newMatch.setPlayer1Id(match.getPlayer1Id());
            } else {
                newMatch.setPlayer2Id(match.getPlayer1Id());
            }
            matchRepository.save(newMatch);
        });
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

    public void updateMatchScore(Integer matchId, ScoreCreateRequestDto scoreDto) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
        match.setScore(scoreDto.getScore());
        Integer winnerId = scoreDto.player1() > scoreDto.player2() ? match.getPlayer1Id() : match.getPlayer2Id();
        match.setWinnerId(winnerId);
        match.setStatus(MatchStatus.COMPLETED);
        matchRepository.save(match);
        bracketService.assignWinnerAndRefereeToNextMatch(matchId, winnerId, match.getRefereeId());
    }

    public void deleteMatch(Integer matchId) {
        matchRepository.deleteById(matchId);
    }

    public Map<MatchStatus, List<MatchResponseDto>> getMatchesByCompetitionIdGroupedByStatus(Integer competitionId) {
        List<MatchResponseDto> matches = matchRepository.findByCompetitionCompetitionId(competitionId)
                .stream()
                .map(MatchResponseDto::fromModel)
                .toList();

        Map<MatchStatus, List<MatchResponseDto>> grouped = matches
                .stream()
                .collect(Collectors.groupingBy(MatchResponseDto::status));

        return grouped;
    }
}
