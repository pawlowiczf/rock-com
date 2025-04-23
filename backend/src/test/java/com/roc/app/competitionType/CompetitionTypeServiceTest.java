package com.roc.app.competitionType;

import com.roc.app.competitionType.dto.CompetitionTypeDTO;
import com.roc.app.competitionType.exception.CompetitionTypeNotFoundException;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionTypeServiceTest {

    @Mock
    private CompetitionTypeRepository competitionTypeRepository;

    @InjectMocks
    private CompetitionTypeService competitionTypeService;

    private CompetitionType testType;

    @BeforeEach
    void setUp() {
        testType = new CompetitionType();
        testType.setTypeId(1);
        testType.setTypeLabel("Tennis");
    }

    @Test
    void getAllTypes_ShouldReturnAllTypes() {
        // Given
        List<CompetitionType> types = Arrays.asList(testType);
        when(competitionTypeRepository.findAll()).thenReturn(types);

        // When
        List<CompetitionTypeDTO> result = competitionTypeService.getAllCompetitionTypes();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTypeId()).isEqualTo(1);
        assertThat(result.get(0).getTypeLabel()).isEqualTo("Tennis");
        verify(competitionTypeRepository, times(1)).findAll();
    }

    @Test
    void getTypeById_WithValidId_ShouldReturnType() {

        when(competitionTypeRepository.findById(1)).thenReturn(Optional.of(testType));

        CompetitionTypeDTO result = competitionTypeService.getCompetitionTypeById(1);

        assertThat(result).isNotNull();
        assertThat(result.getTypeId()).isEqualTo(1);
        assertThat(result.getTypeLabel()).isEqualTo("Tennis");
        verify(competitionTypeRepository, times(1)).findById(1);
    }

    @Test
    void getTypeById_WithInvalidId_ShouldThrowException() {

        when(competitionTypeRepository.findById(999)).thenReturn(Optional.empty());

        CompetitionTypeNotFoundException exception = assertThrows(
                CompetitionTypeNotFoundException.class,
                () -> competitionTypeService.getCompetitionTypeById(999)
        );

        assertThat(exception.getMessage()).contains("Competition type not found with id: 999");
        verify(competitionTypeRepository, times(1)).findById(999);
    }
}