package com.roc.app.bracket;

import com.roc.app.match.Match;
import com.roc.app.match.MatchRepository;
import com.roc.app.match.exception.MatchNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BracketService {

    private final BracketRepository bracketRepository;
    private final MatchRepository matchRepository;

    public void saveRound(List<Match> matches, List<Match> nextMatches) {
        Random random = new Random();
        for (Match nextMatch : nextMatches) {
            for (int i = 0; i < 2; i++) {
                Match match = matches.remove(random.nextInt(matches.size()));
                Bracket bracket = Bracket.builder()
                        .nextMatchId(nextMatch.getMatchId())
                        .matchId(match.getMatchId())
                        .build();
                bracketRepository.save(bracket);
            }
        }
    }

    public void assignWinnerAndRefereeToNextMatch(Integer matchId, Integer winnerId, Integer refereeId) {
        Integer nextMatchId = bracketRepository.findNextMatchIdByMatchId(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
        Match nextMatch = matchRepository.findById(nextMatchId)
                .orElseThrow(() -> new MatchNotFoundException(nextMatchId));
        if (nextMatch.getPlayer1Id() == null) {
            nextMatch.setPlayer1Id(winnerId);
        } else {
            nextMatch.setPlayer2Id(winnerId);
        }
        if (nextMatch.getRefereeId() == null) {
            nextMatch.setRefereeId(refereeId);
        }
        matchRepository.save(nextMatch);
    }
}
