package com.roc.app.competition.assignment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompetitionParticipantNotFoundException extends RuntimeException {

    public CompetitionParticipantNotFoundException(CompetitionParticipant.CompetitionParticipantId id){
        super("Competition Participant " + id + "not found");
    }

}
