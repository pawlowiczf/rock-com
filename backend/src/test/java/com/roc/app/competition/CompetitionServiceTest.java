package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionDTO;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import com.roc.app.competitionType.CompetitionType;
import com.roc.app.competitionType.CompetitionTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    private CompetitionTypeRepository typeRepository;

    @InjectMocks
    private CompetitionService competitionService;

    private Competition testCompetition;
    private CompetitionType testType;

    @BeforeEach
    void setUp() {
        testType = new CompetitionType();
        testType.setTypeId(1);
        testType.setTypeLabel("Tennis");

        testCompetition = new Competition();
        testCompetition.setCompetitionId(1L);
        testCompetition.setCompetitionType(testType);
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
        List<CompetitionDTO> result = competitionService.getAllCompetitions();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompetitionId()).isEqualTo(1L);
        verify(competitionRepository, times(1)).findAll();
    }

    @Test
    void getCompetitionById_WithValidId_ShouldReturnCompetition() {

        when(competitionRepository.findById(1L)).thenReturn(Optional.of(testCompetition));

        CompetitionDTO result = competitionService.getCompetitionById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getCompetitionId()).isEqualTo(1);
        verify(competitionRepository, times(1)).findById(1L);
    }

    @Test
    void getCompetitionById_WithInvalidId_ShouldThrowException() {
        when(competitionRepository.findById(999L)).thenReturn(Optional.empty());

        CompetitionNotFoundException exception = assertThrows(
                CompetitionNotFoundException.class,
                () -> competitionService.getCompetitionById(999L)
        );

        assertThat(exception.getMessage()).contains("Competition not found with id: 999");
        verify(competitionRepository, times(1)).findById(999L);
    }
}