package com.roc.app.competitionType;


import com.roc.app.competitionType.dto.CompetitionTypeDTO;
import com.roc.app.competitionType.exception.CompetitionTypeNotFoundException;
import com.roc.app.competitionType.exception.DuplicateCompetitionTypeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompetitionTypeService {

    private final CompetitionTypeRepository competitionTypeRepository;

    public List<CompetitionTypeDTO> getAllCompetitionTypes() {
        return competitionTypeRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CompetitionTypeDTO getCompetitionTypeById(Integer id) {
        return competitionTypeRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() ->  {
                    return new CompetitionTypeNotFoundException("Competition type not found with id: " + id);
                });
    }

    @Transactional
    public CompetitionTypeDTO createCompetitionType(CompetitionTypeDTO competitionTypeDTO) {
        if (competitionTypeRepository.existsByTypeLabel(competitionTypeDTO.getTypeLabel())) {
            throw new DuplicateCompetitionTypeException("Competition type with label '" + competitionTypeDTO.getTypeLabel() + "' already exists");
        }

        CompetitionType competitionType = mapToEntity(competitionTypeDTO);
        CompetitionType savedCompetitionType = competitionTypeRepository.save(competitionType);
        return mapToDto(savedCompetitionType);
    }

    @Transactional
    public CompetitionTypeDTO updateCompetitionType(Integer id, CompetitionTypeDTO competitionTypeDTO) {
        CompetitionType competitionType = competitionTypeRepository.findById(id)
                .orElseThrow(() -> new CompetitionTypeNotFoundException("Competition type not found with id: " + id));

        if (!competitionType.getTypeLabel().equals(competitionTypeDTO.getTypeLabel()) &&
                competitionTypeRepository.existsByTypeLabel(competitionTypeDTO.getTypeLabel())) {
            throw new DuplicateCompetitionTypeException("Competition type with label '" + competitionTypeDTO.getTypeLabel() + "' already exists");
        }

        competitionType.setTypeLabel(competitionTypeDTO.getTypeLabel());
        CompetitionType updatedCompetitionType = competitionTypeRepository.save(competitionType);
        return mapToDto(updatedCompetitionType);
    }

    @Transactional
    public void deleteCompetitionType(Integer id) {
        if (!competitionTypeRepository.existsById(id)) {
            throw new CompetitionTypeNotFoundException("Competition type not found with id: " + id);
        }
        competitionTypeRepository.deleteById(id);
    }

    private CompetitionTypeDTO mapToDto(CompetitionType competitionType) {
        return CompetitionTypeDTO.builder()
                .typeId(competitionType.getTypeId())
                .typeLabel(competitionType.getTypeLabel())
                .build();
    }

    private CompetitionType mapToEntity(CompetitionTypeDTO dto) {
        return CompetitionType.builder()
                .typeId(dto.getTypeId())
                .typeLabel(dto.getTypeLabel())
                .build();
    }
}
