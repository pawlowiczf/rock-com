package com.roc.app.competition.assignment;

import com.roc.app.competition.Competition;
import com.roc.app.competition.assignment.dto.CompetitionParticipantResponseDto;
import com.roc.app.user.participant.Participant;
import com.roc.app.user.participant.ParticipantStatus;
import com.roc.app.user.participant.dto.ParticipantResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CompetitionParticipantService {
    private final CompetitionParticipantRepository competitionParticipantRepository;

    public CompetitionParticipantResponseDto enroll(Competition competition, Participant participant) {
        Integer competitionId = competition.getCompetitionId();

        if (!competition.getRegistrationOpen()) {
            throw new RegistrationIsClosedException(competitionId);
        }

        if (competitionParticipantRepository.isEnrolled(competitionId, participant.getUserId())) {
            throw new ParticipantAlreadyEnrolledException(participant.getUserId(), competitionId);
        }

        CompetitionParticipant competitionParticipant = new CompetitionParticipant(
                competitionId,
                participant.getUserId(),
                ParticipantStatus.WAITING_LIST,
                LocalDateTime.now()
        );

        if(competition.getParticipantsLimit() > competitionParticipantRepository.countByCompetitionId(competitionId)) {
            competitionParticipant.setParticipantStatus(ParticipantStatus.CONFIRMED);
        }

        competitionParticipantRepository.save(competitionParticipant);

        ParticipantResponseDto participantResponseDto = ParticipantResponseDto.fromModel(participant);

        return CompetitionParticipantResponseDto.fromModel(competitionParticipant, participantResponseDto);
    }
}
