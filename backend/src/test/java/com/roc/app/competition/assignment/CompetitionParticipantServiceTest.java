package com.roc.app.competition.assignment;

import com.roc.app.competition.Competition;
import com.roc.app.competition.assignment.dto.CompetitionParticipantResponseDto;
import com.roc.app.user.participant.Participant;
import com.roc.app.user.participant.ParticipantStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompetitionParticipantServiceTest {

    @Mock
    private CompetitionParticipantRepository competitionParticipantRepository;

    @InjectMocks
    private CompetitionParticipantService competitionParticipantService;

    @Test
    void shouldResignate(){
        //given
        LocalDateTime statusChangeDate = LocalDateTime.now().minusDays(1);
        Competition competition = new Competition();
        competition.setCompetitionId(1);
        Participant participant = new Participant();
        participant.setUserId(1L);
        CompetitionParticipant competitionParticipant = new CompetitionParticipant(competition.getCompetitionId(),
                participant.getUserId(),
                ParticipantStatus.WAITING_LIST,statusChangeDate);

        when(competitionParticipantRepository.findById(any()))
                .thenReturn(Optional.of(competitionParticipant));
        //when
        CompetitionParticipantResponseDto result = competitionParticipantService.resignation(competition, participant);
        //then
        assertNotNull(result);
        assertEquals(competition.getCompetitionId(),result.competitionId());
        assertNotNull(result.participant());
        assertEquals(participant.getUserId(),result.participant().userId());
        assertEquals(ParticipantStatus.WITHDRAWN,result.participantStatus());
        assertNotEquals(statusChangeDate,result.statusChangedAt());
    }

    @Test
    void shouldNotResignate(){
        //given
        LocalDateTime statusChangeDate = LocalDateTime.now();
        Competition competition = new Competition();
        competition.setCompetitionId(1);
        Participant participant = new Participant();
        participant.setUserId(1L);
        CompetitionParticipant competitionParticipant = new CompetitionParticipant(competition.getCompetitionId(),
                participant.getUserId(),
                ParticipantStatus.WITHDRAWN,statusChangeDate);

        when(competitionParticipantRepository.findById(any()))
                .thenReturn(Optional.of(competitionParticipant));
        //when
        Throwable throwable = catchThrowable(() -> competitionParticipantService.resignation(competition, participant));
        //then
        assertEquals(ParticipantAlreadyWithdrawnException.class,throwable.getClass());
    }


}