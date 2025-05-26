package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionResponseDto;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @InjectMocks
    private CompetitionService competitionService;

    private Competition testCompetition;

    @BeforeEach
    void setUp() {
        testCompetition = new Competition();
        testCompetition.setCompetitionId(1);
        testCompetition.setType(CompetitionType.BADMINTON);
        testCompetition.setMatchDurationMinutes(60);
        testCompetition.setAvailableCourts(4);
        testCompetition.setParticipantsLimit(16);
        testCompetition.setStreetAddress("Nowa 3");
        testCompetition.setCity("Warszawa");
        testCompetition.setPostalCode("01-111");
        testCompetition.setRegistrationOpen(false);
    }

    @Test
    void getAllCompetitions_ShouldReturnAllCompetitions() {
        // Given
        List<Competition> competitions = Arrays.asList(testCompetition);
        when(competitionRepository.findAll()).thenReturn(competitions);

        // When
        List<CompetitionResponseDto> result = competitionService.getAllCompetitions();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).competitionId()).isEqualTo(1);
        verify(competitionRepository, times(1)).findAll();
    }

    @Test
    void getCompetitionById_WithValidId_ShouldReturnCompetition() {

        when(competitionRepository.findById(1)).thenReturn(Optional.of(testCompetition));

        CompetitionResponseDto result = competitionService.getCompetitionById(1);

        assertThat(result).isNotNull();
        assertThat(result.competitionId()).isEqualTo(1);
        verify(competitionRepository, times(1)).findById(1);
    }

    @Test
    void getCompetitionById_WithInvalidId_ShouldThrowException() {
        when(competitionRepository.findById(999)).thenReturn(Optional.empty());

        CompetitionNotFoundException exception = assertThrows(
                CompetitionNotFoundException.class,
                () -> competitionService.getCompetitionById(999)
        );

        assertThat(exception.getMessage()).contains("Competition not found with id: 999");
        verify(competitionRepository, times(1)).findById(999);
    }
}