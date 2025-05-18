package com.roc.app.match.exception;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(Integer id) {
        super("Match with ID " + id + " not found.");
    }
}
