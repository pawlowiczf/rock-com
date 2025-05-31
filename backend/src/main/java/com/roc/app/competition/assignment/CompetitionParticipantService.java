package com.roc.app.competition.assignment;

import com.roc.app.competition.Competition;
import com.roc.app.competition.assignment.dto.CompetitionParticipantResponseDto;
import com.roc.app.competition.assignment.dto.CompetitionParticipantsListResponseDto;
import com.roc.app.user.general.exception.UserNotFoundException;
import com.roc.app.user.participant.Participant;
import com.roc.app.user.participant.ParticipantRepository;
import com.roc.app.user.participant.ParticipantStatus;
import com.roc.app.user.participant.dto.ParticipantResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CompetitionParticipantService {
    private final CompetitionParticipantRepository competitionParticipantRepository;
    private final ParticipantRepository participantRepository;

    public CompetitionParticipantResponseDto enroll(Competition competition, Participant participant) {
        Integer competitionId = competition.getCompetitionId();

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

    public List<CompetitionParticipantResponseDto> listUsers(Integer competitionId) {
        List<CompetitionParticipant> participants = competitionParticipantRepository.findCompetitionParticipantsByCompetitionId(competitionId);

        return participants.stream()
                .map(p -> {
                    Participant participant = participantRepository.findById(p.getParticipantId())
                            .orElseThrow(() -> new UserNotFoundException(p.getParticipantId()));
                    return CompetitionParticipantResponseDto.fromModel(p, ParticipantResponseDto.fromModel(participant));
                }).collect(Collectors.toList());
    }

    public CompetitionParticipantsListResponseDto listCompetitionParticipants(Integer competitionId) {
        var competitionParticipants = listUsers(competitionId)
                .stream()
                .collect(Collectors.groupingBy(CompetitionParticipantResponseDto::participantStatus));

        var confirmed = filterParticipantsByStatus(competitionParticipants, ParticipantStatus.CONFIRMED);
        var waiting = filterParticipantsByStatus(competitionParticipants, ParticipantStatus.WAITING_LIST);


        return new CompetitionParticipantsListResponseDto(
                confirmed,
                waiting
        );
    }

    private List<ParticipantResponseDto> filterParticipantsByStatus(Map<ParticipantStatus, List<CompetitionParticipantResponseDto>> list, ParticipantStatus status) {
        return list.getOrDefault(status, List.of()).stream()
                .map(CompetitionParticipantResponseDto::participant)
                .toList();
    }
}
