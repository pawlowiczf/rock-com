package com.roc.app.match;

import com.roc.app.competition.Competition;
import com.roc.app.competition.CompetitionRepository;
import com.roc.app.match.dto.MatchCreateRequestDto;
import com.roc.app.match.dto.MatchUpdateRequestDto;
import com.roc.app.match.exception.MatchNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private CompetitionRepository competitionRepository;

    @InjectMocks
    private MatchService matchService;

    private MatchCreateRequestDto createDto;
    private MatchUpdateRequestDto updateDto;

    @BeforeEach
    void setUp() {
        createDto = new MatchCreateRequestDto(
                1, // competitionId
                10, // player1Id
                20, // player2Id
                30, // refereeId
                LocalDateTime.of(2025, 5, 16, 14, 0),
                MatchStatus.SCHEDULED,
                null,
                null
        );

        updateDto = new MatchUpdateRequestDto(
                11, // player1Id
                21, // player2Id
                31, // refereeId
                LocalDateTime.of(2025, 5, 17, 15, 0),
                MatchStatus.COMPLETED,
                "21-15",
                11 // winnerId
        );
    }

    @Test
    void createMatch_savesAndReturnsId() {
        Competition mockCompetition = new Competition();
        mockCompetition.setCompetitionId(createDto.competitionId());

        when(competitionRepository.findById(createDto.competitionId()))
                .thenReturn(Optional.of(mockCompetition));

        Match savedMatch = Match.builder()
                .matchId(100)
                .competition(mockCompetition)
                .player1Id(createDto.player1Id())
                .player2Id(createDto.player2Id())
                .refereeId(createDto.refereeId())
                .matchDate(createDto.matchDate())
                .status(createDto.status())
                .score(createDto.score())
                .winnerId(createDto.winnerId())
                .build();

        when(matchRepository.save(any(Match.class))).thenReturn(savedMatch);

        Integer matchId = matchService.createMatch(createDto);

        assertThat(matchId).isEqualTo(100);

        ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchCaptor.capture());

        Match captured = matchCaptor.getValue();
        assertThat(captured.getCompetition().getCompetitionId()).isEqualTo(createDto.competitionId());
        assertThat(captured.getPlayer1Id()).isEqualTo(createDto.player1Id());
        assertThat(captured.getPlayer2Id()).isEqualTo(createDto.player2Id());
        assertThat(captured.getRefereeId()).isEqualTo(createDto.refereeId());
        assertThat(captured.getMatchDate()).isEqualTo(createDto.matchDate());
        assertThat(captured.getStatus()).isEqualTo(createDto.status());
    }


    @Test
    void updateMatch_updatesExistingMatch() {
        Match existingMatch = Match.builder()
                .matchId(200)
                .competition(new Competition(1))
                .player1Id(10)
                .player2Id(20)
                .refereeId(30)
                .matchDate(LocalDateTime.of(2025, 5, 15, 14, 0))
                .status(MatchStatus.COMPLETED)
                .score(null)
                .winnerId(null)
                .build();

        when(matchRepository.findById(200)).thenReturn(Optional.of(existingMatch));
        when(matchRepository.save(any(Match.class))).thenReturn(existingMatch);

        matchService.updateMatch(200, updateDto);

        verify(matchRepository).findById(200);
        verify(matchRepository).save(existingMatch);

        assertThat(existingMatch.getPlayer1Id()).isEqualTo(updateDto.player1Id());
        assertThat(existingMatch.getPlayer2Id()).isEqualTo(updateDto.player2Id());
        assertThat(existingMatch.getRefereeId()).isEqualTo(updateDto.refereeId());
        assertThat(existingMatch.getMatchDate()).isEqualTo(updateDto.matchDate());
        assertThat(existingMatch.getStatus()).isEqualTo(updateDto.status());
        assertThat(existingMatch.getScore()).isEqualTo(updateDto.score());
        assertThat(existingMatch.getWinnerId()).isEqualTo(updateDto.winnerId());
    }

    @Test
    void updateMatch_throwsExceptionIfMatchNotFound() {
        when(matchRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(MatchNotFoundException.class, () -> matchService.updateMatch(999, updateDto));

        verify(matchRepository).findById(999);
        verify(matchRepository, never()).save(any());
    }

    @Test
    void deleteMatch_callsRepositoryDelete() {

        doNothing().when(matchRepository).deleteById(300);

        matchService.deleteMatch(300);

        verify(matchRepository).deleteById(300);
    }
}