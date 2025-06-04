package com.roc.app.competition.assignment;

public class RegistrationIsClosedException extends RuntimeException {
    public RegistrationIsClosedException(Integer competitionId) {
        super("Registration for competition " + competitionId + " is closed.");
    }
}
