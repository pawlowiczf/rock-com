package com.roc.app.competition.exception;

public class CompetitionNotFoundException extends RuntimeException {
    public CompetitionNotFoundException(Integer id) {
        super("Competition not found with id: " + id);
    }
}
