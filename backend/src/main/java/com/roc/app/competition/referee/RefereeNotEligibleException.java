package com.roc.app.competition.referee;

public class RefereeNotEligibleException extends RuntimeException {
    public RefereeNotEligibleException(Long refereeId) {
            super("Referee with id " + refereeId + " doesn't have proper permissions (licence type) to referee the competition");
    }
}

