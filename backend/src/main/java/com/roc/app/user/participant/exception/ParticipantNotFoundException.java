package com.roc.app.user.participant.exception;

public class ParticipantNotFoundException extends RuntimeException {
    public ParticipantNotFoundException(String message) {
        super(message);
    }

    public ParticipantNotFoundException(Integer id) {
        super("Participant with id " + id + " not found");
    }
}
