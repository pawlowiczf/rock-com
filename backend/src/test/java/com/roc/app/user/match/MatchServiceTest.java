package com.roc.app.user.match;

import com.roc.app.match.MatchService;
import com.roc.app.match.dto.ParticipantMatchResponseDto;
import com.roc.app.match.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    private List<ParticipantMatchResponseDto> mockMatches;

    @BeforeEach
    void setUp() {
        mockMatches = List.of(
                new ParticipantMatchResponseDto(
                        101, 1, LocalDateTime.of(2025, 5, 1, 15, 30),
                        "completed", "Alice Smith", "21-18", true
                ),
                new ParticipantMatchResponseDto(
                        102, 1, LocalDateTime.of(2025, 4, 20, 11, 0),
                        "completed", "Bob Johnson", "21-17", false
                ),
                new ParticipantMatchResponseDto(
                        103, 2, LocalDateTime.of(2025, 4, 15, 10, 0),
                        "scheduled", "Charlie Davis", null, false
                ),
                new ParticipantMatchResponseDto(
                        104, 2, LocalDateTime.of(2025, 5, 3, 9, 0),
                        "completed", "Alice Smith", "22-20", true
                )
        );
    }

    @Test
    void getParticipantMatches_returnsMatchesForGivenParticipantOnly() {
        // Given
        Integer userId = 5;
        when(matchRepository.findParticipantMatches(userId)).thenReturn(mockMatches);

        // When
        List<ParticipantMatchResponseDto> result = matchService.getParticipantMatches(userId);

        // Then
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getMatchId()).isEqualTo(101);
        assertThat(result.get(1).getMatchId()).isEqualTo(102);
        assertThat(result.get(2).getMatchId()).isEqualTo(103);
        assertThat(result.get(3).getMatchId()).isEqualTo(104);
    }

    @Test
    void getParticipantMatches_emptyListWhenParticipantHasNoMatches() {
        // Given
        Integer userId = 99;
        when(matchRepository.findParticipantMatches(userId)).thenReturn(List.of());

        // When
        List<ParticipantMatchResponseDto> result = matchService.getParticipantMatches(userId);

        // Then
        assertThat(result).isEmpty();
    }
}
