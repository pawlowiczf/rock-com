package com.roc.app.bracket;

import com.roc.app.match.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BracketService {

    private final BracketRepository bracketRepository;

    public void generateBracketConnections(List<Match> matches, List<Match> nextMatches) {
        Random random = new Random();
        for(Match nextMatch : nextMatches) {
            for(int i = 0; i < 2; i++) {
                Match match = matches.remove(random.nextInt(matches.size()));
                Bracket bracket = Bracket.builder()
                        .nextMatchId(nextMatch.getMatchId())
                        .matchId(match.getMatchId())
                        .build();
                bracketRepository.save(bracket);
            }
        }
    }
}
