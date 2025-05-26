package com.roc.app.competition.assignment;

public class ParticipantAlreadyEnrolledException extends RuntimeException {
    public ParticipantAlreadyEnrolledException(Long participantId, Integer competitionId) {
        super("Participant with id " + participantId + " is already enrolled into competition with id " + competitionId);
    }
}
