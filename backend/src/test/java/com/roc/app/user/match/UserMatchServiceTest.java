package com.roc.app.user.match;

import com.roc.app.match.UserMatchService;
import com.roc.app.match.dto.UserMatchDto;
import com.roc.app.match.UserMatchRepository;
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
class UserMatchServiceTest {

    @Mock
    private UserMatchRepository userMatchRepository;

    @InjectMocks
    private UserMatchService userMatchService;

    private List<UserMatchDto> mockMatches;

    @BeforeEach
    void setUp() {
        mockMatches = List.of(
                new UserMatchDto(
                        101, 1, LocalDateTime.of(2025, 5, 1, 15, 30),
                        "completed", "Alice Smith", "21-18", true
                ),
                new UserMatchDto(
                        102, 1, LocalDateTime.of(2025, 4, 20, 11, 0),
                        "completed", "Bob Johnson", "21-17", false
                ),
                new UserMatchDto(
                        103, 2, LocalDateTime.of(2025, 4, 15, 10, 0),
                        "scheduled", "Charlie Davis", null, false
                ),
                new UserMatchDto(
                        104, 2, LocalDateTime.of(2025, 5, 3, 9, 0),
                        "completed", "Alice Smith", "22-20", true
                )
        );
    }

    @Test
    void getUserMatches_returnsMatchesForGivenUserOnly() {
        // Given
        Integer userId = 5;
        when(userMatchRepository.findUserMatches(userId)).thenReturn(mockMatches);

        // When
        List<UserMatchDto> result = userMatchService.getUserMatches(userId);

        // Then
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getMatchId()).isEqualTo(101);
        assertThat(result.get(1).getMatchId()).isEqualTo(102);
        assertThat(result.get(2).getMatchId()).isEqualTo(103);
        assertThat(result.get(3).getMatchId()).isEqualTo(104);
    }

    @Test
    void getUserMatches_emptyListWhenUserHasNoMatches() {
        // Given
        Integer userId = 99;
        when(userMatchRepository.findUserMatches(userId)).thenReturn(List.of());

        // When
        List<UserMatchDto> result = userMatchService.getUserMatches(userId);

        // Then
        assertThat(result).isEmpty();
    }
}
