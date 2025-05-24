package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionDateCreateRequestDto;
import com.roc.app.competition.dto.CompetitionDateResponseDto;
import com.roc.app.competition.dto.CompetitionDateUpdateRequestDto;
import com.roc.app.competition.exception.CompetitionDateNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompetitionDateServiceTest {
    @Mock
    private CompetitionDateRepository dateRepository;
    @Mock
    private CompetitionRepository competitionRepository;
    @InjectMocks
    private CompetitionDateService dateService;

    private static final Integer COMPETITION_ID = 1;
    private static final Integer DATE_ID = 1;
    private CompetitionDate competitionDate1;
    private Competition competition;

    @BeforeEach
    void setUp() {
        competition = Competition.builder()
                .competitionId(COMPETITION_ID)
                .build();

        competitionDate1 = CompetitionDate.builder()
                .dateId(DATE_ID)
                .competition(competition)
                .startTime(LocalDateTime.of(2026, 6, 10, 10, 0))
                .endTime(LocalDateTime.of(2026, 6, 10, 18, 0))
                .build();
    }


    @Test
    void updateDate_shouldUpdateExistingDate() {
        // Given
        when(dateRepository.findById(DATE_ID)).thenReturn(Optional.of(competitionDate1));
        LocalDateTime newStartTime = LocalDateTime.of(2026, 6, 5, 10, 0);
        LocalDateTime newEndTime = LocalDateTime.of(2026, 6, 5, 18, 0);
        CompetitionDateUpdateRequestDto dto = new CompetitionDateUpdateRequestDto(
                newStartTime,
                newEndTime
        );
        CompetitionDate expectedDate = CompetitionDate.builder()
                .dateId(DATE_ID)
                .competition(competition)
                .startTime(newStartTime)
                .endTime(newEndTime)
                .build();
        // When
        dateService.updateDate(DATE_ID, dto);
        //Then
        verify(dateRepository).save(expectedDate);
    }

    @Test
    void updateDate_shouldThrowExceptionForNotExistingDate() {
        // Given
        when(dateRepository.findById(DATE_ID)).thenThrow(new CompetitionDateNotFoundException(DATE_ID));
        CompetitionDateUpdateRequestDto dto = new CompetitionDateUpdateRequestDto(
                LocalDateTime.of(2026, 6, 5, 10, 0),
                LocalDateTime.of(2026, 6, 5, 18, 0)
        );
        // When
        //Then
        assertThrows(CompetitionDateNotFoundException.class, () -> dateService.updateDate(DATE_ID, dto));
    }

    @Test
    void deleteDate_shouldDeleteExistingDate() {
        // Given
        when(dateRepository.findById(DATE_ID)).thenReturn(Optional.of(competitionDate1));
        // When
        dateService.deleteDate(DATE_ID);
        // Then
        verify(dateRepository).delete(competitionDate1);
    }

    @Test
    void deleteDate_shouldThrowExceptionForNotExistingDate() {
        // Given
        when(dateRepository.findById(DATE_ID)).thenThrow(new CompetitionDateNotFoundException(DATE_ID));
        // When
        // Then
        assertThrows(CompetitionDateNotFoundException.class, () -> dateService.deleteDate(DATE_ID));
    }

    @Test
    void getCompetitionDates_shouldReturnAllCompetitionDates() {
        // Given
        CompetitionDate competitionDate2 = CompetitionDate.builder()
                .dateId(2)
                .competition(competition)
                .startTime(LocalDateTime.of(2026, 6, 12, 10, 0))
                .endTime(LocalDateTime.of(2026, 6, 12, 18, 0))
                .build();
        CompetitionDate competitionDate3 = CompetitionDate.builder()
                .dateId(3)
                .competition(competition)
                .startTime(LocalDateTime.of(2026, 6, 8, 10, 0))
                .endTime(LocalDateTime.of(2026, 6, 8, 18, 0))
                .build();
        List<CompetitionDateResponseDto> expectedResult = List.of(
                new CompetitionDateResponseDto(
                        competitionDate3.getDateId(),
                        competitionDate3.getCompetition().getCompetitionId(),
                        competitionDate3.getStartTime(),
                        competitionDate3.getEndTime()
                ),
                new CompetitionDateResponseDto(
                        competitionDate1.getDateId(),
                        competitionDate1.getCompetition().getCompetitionId(),
                        competitionDate1.getStartTime(),
                        competitionDate1.getEndTime()
                ),
                new CompetitionDateResponseDto(
                        competitionDate2.getDateId(),
                        competitionDate2.getCompetition().getCompetitionId(),
                        competitionDate2.getStartTime(),
                        competitionDate2.getEndTime()
                )
        );
        when(dateRepository.findAllByCompetitionId(competition.getCompetitionId())).thenReturn(List.of(competitionDate3, competitionDate1, competitionDate2));
        // When
        List<CompetitionDateResponseDto> result = dateService.getCompetitionDates(competition.getCompetitionId());
        // Then
        assertEquals(expectedResult, result);
    }


}
