package com.roc.app.competition.assignment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ParticipantAlreadyWithdrawnException extends RuntimeException{
    public ParticipantAlreadyWithdrawnException(CompetitionParticipant.CompetitionParticipantId id){
        super("Competition Participant " + id + "already withdrawn");
    }
}
