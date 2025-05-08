package com.roc.app.competition.exception;

public class CompetitionTypeNotFoundException extends RuntimeException {

    public CompetitionTypeNotFoundException(String competitionType) {
        super("Competition type not found with id: " + competitionType);
    }
}
