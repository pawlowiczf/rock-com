package com.roc.app.competition.assignment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ParticipantAlreadyEnrolledException extends RuntimeException {
    public ParticipantAlreadyEnrolledException(Long participantId, Integer competitionId) {
        super("Participant with id " + participantId + " is already enrolled into competition with id " + competitionId);
    }
}
