package com.roc.app.bracket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BracketService {

    private final BracketRepository bracketRepository;

    public void saveRound(List<Integer> matchIds, List<Integer> nextMatchIds) {
        Random random = new Random();
        for(Integer nextMatchId : nextMatchIds) {
            for(int i = 0; i < 2; i++) {
                Integer matchId = matchIds.remove(random.nextInt(matchIds.size()));
                Bracket bracket = Bracket.builder()
                        .nextMatchId(nextMatchId)
                        .matchId(matchId)
                        .build();
                bracketRepository.save(bracket);
            }
        }
    }
}
