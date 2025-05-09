package com.roc.app.user.referee.exception;

public class RefereeNotFoundException extends RuntimeException {
    public RefereeNotFoundException(String message) {super(message);}
    public RefereeNotFoundException(Long id) {
        this("Referee not found. Id: " + id);
    }
}
