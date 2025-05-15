package com.roc.app.match;

import com.roc.app.match.dto.*;
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
}