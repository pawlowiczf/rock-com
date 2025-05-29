package com.roc.app.competition.exception;

public class CompetitionDateNotFoundException extends RuntimeException {
    public CompetitionDateNotFoundException(Integer id) {
        super("Competition date not found with id: " + id);
    }
}
