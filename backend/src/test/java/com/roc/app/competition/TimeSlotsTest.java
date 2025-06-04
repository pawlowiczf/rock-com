package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionDateResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TimeSlotsTest {
    @Test
    public void TimeSlots_ShouldCreateCorrectSlots(){
        // Given
        int availableCourts  = 10;
        Competition competition = Competition.builder()
                .matchDurationMinutes(120)
                .availableCourts(availableCourts)
                .build();
        List<CompetitionDateResponseDto> dates = List.of(
                new CompetitionDateResponseDto(
                        1,
                        1,
                        LocalDateTime.of(2030, 5, 30, 8, 0),
                        LocalDateTime.of(2030, 5, 30, 13, 0)
                ),
                new CompetitionDateResponseDto(
                        2,
                        1,
                        LocalDateTime.of(2030, 5, 31, 8, 0),
                        LocalDateTime.of(2030, 5, 31, 13, 0)
                )
        );
        TreeMap<LocalDateTime, Integer> expectedSlots = new TreeMap<>();
        expectedSlots.put(LocalDateTime.of(2030, 5, 30, 8, 0), availableCourts);
        expectedSlots.put(LocalDateTime.of(2030, 5, 30, 10, 0), availableCourts);
        expectedSlots.put(LocalDateTime.of(2030, 5, 31, 8, 0), availableCourts);
        expectedSlots.put(LocalDateTime.of(2030, 5, 31, 10, 0), availableCourts);

        // When
        TimeSlots timeSlots = new TimeSlots(competition, dates);

        // Then
        assertEquals(expectedSlots, timeSlots.getSlots());
    }

    @Test
    public void getNext_ShouldReturnNextAvailableSlot(){
        // Given
        int availableCourts  = 2;
        Competition competition = Competition.builder()
                .matchDurationMinutes(120)
                .availableCourts(availableCourts)
                .build();
        List<CompetitionDateResponseDto> dates = List.of(
                new CompetitionDateResponseDto(
                        1,
                        1,
                        LocalDateTime.of(2030, 5, 30, 8, 0),
                        LocalDateTime.of(2030, 5, 30, 13, 0)
                )
        );
        LocalDateTime first = LocalDateTime.of(2030, 5, 30, 8, 0);
        LocalDateTime second = LocalDateTime.of(2030, 5, 30, 10, 0);
        List<LocalDateTime> expectedSlots = List.of(first, first, second, second);
        TimeSlots timeSlots = new TimeSlots(competition, dates);

        // When
        List<LocalDateTime> results = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            results.add(timeSlots.getNext());
        }

        // Then
        assertEquals(expectedSlots, results);
    }
}
