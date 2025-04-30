package com.roc.app.competition.exception;

public class CompetitionNotFoundException extends RuntimeException {
    public CompetitionNotFoundException(Long id) {
        super("Competition not found with id: " + id);
    }
}
