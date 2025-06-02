package com.roc.app.competition.referee;

public class RefereeNotEligibleException extends RuntimeException {
  public RefereeNotEligibleException(String message) {
    super(message);
  }
}
