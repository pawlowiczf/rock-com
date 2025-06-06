package com.roc.app.user.referee.exception;

public class RefereeNotFoundException extends RuntimeException {
    public RefereeNotFoundException(Integer id) {
        super("Referee not found. Id: " + id);
    }
}
