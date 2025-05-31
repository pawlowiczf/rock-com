package com.roc.app.user.general.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Integer id) {
        super("User with id " + id + " not found");
    }
}
