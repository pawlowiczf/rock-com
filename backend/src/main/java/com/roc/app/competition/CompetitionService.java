package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionDTO;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import com.roc.app.competitionType.CompetitionType;
import com.roc.app.competitionType.CompetitionTypeRepository;
import com.roc.app.competitionType.exception.CompetitionTypeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final CompetitionTypeRepository competitionTypeRepository;

    public List<CompetitionDTO> getAllCompetitions() {
        return competitionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionDTO> getCompetitionsByType(Integer typeId) {
        return competitionRepository.findByCompetitionTypeTypeId(typeId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionDTO> getOpenCompetitions() {
        return competitionRepository.findAllOpenCompetitions().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionDTO> getCompetitionsByCity(String city) {
        return competitionRepository.findCompetitionsByCity(city).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CompetitionDTO getCompetitionById(Long id) {
        return competitionRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new CompetitionNotFoundException("Competition not found with id: " + id));
    }

    @Transactional
    public CompetitionDTO createCompetition(CompetitionDTO competitionDTO) {

        if (competitionDTO.getRegistrationOpen() == null) {
            competitionDTO.setRegistrationOpen(false);
        }

        Competition competition = mapToEntity(competitionDTO);
        Competition savedCompetition = competitionRepository.save(competition);
        return mapToDto(savedCompetition);
    }

    @Transactional
    public CompetitionDTO updateCompetition(Long id, CompetitionDTO competitionDTO) {
        Competition competition = competitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException("Competition not found with id: " + id));

        CompetitionType competitionType = competitionTypeRepository.findById(competitionDTO.getTypeId())
                .orElseThrow(() -> new CompetitionTypeNotFoundException("Competition type not found with id: " + competitionDTO.getTypeId()));

        competition.setCompetitionType(competitionType);
        competition.setMatchDurationMinutes(competitionDTO.getMatchDurationMinutes());
        competition.setAvailableCourts(competitionDTO.getAvailableCourts());
        competition.setParticipantsLimit(competitionDTO.getParticipantsLimit());
        competition.setStreetAddress(competitionDTO.getStreetAddress());
        competition.setCity(competitionDTO.getCity());
        competition.setPostalCode(competitionDTO.getPostalCode());

        if (competitionDTO.getRegistrationOpen() != null) {
            competition.setRegistrationOpen(competitionDTO.getRegistrationOpen());
        }

        Competition updatedCompetition = competitionRepository.save(competition);
        return mapToDto(updatedCompetition);
    }


    @Transactional
    public void deleteCompetition(Long id) {
        if (!competitionRepository.existsById(id)) {
            throw new CompetitionNotFoundException("Competition not found with id: " + id);
        }
        competitionRepository.deleteById(id);
    }

    private CompetitionDTO mapToDto(Competition competition) {
        return CompetitionDTO.builder()
                .competitionId(competition.getCompetitionId())
                .typeId(competition.getCompetitionType().getTypeId())
                .typeLabel(competition.getCompetitionType().getTypeLabel())
                .matchDurationMinutes(competition.getMatchDurationMinutes())
                .availableCourts(competition.getAvailableCourts())
                .participantsLimit(competition.getParticipantsLimit())
                .streetAddress(competition.getStreetAddress())
                .city(competition.getCity())
                .postalCode(competition.getPostalCode())
                .registrationOpen(competition.getRegistrationOpen())
                .build();
    }

    private Competition mapToEntity(CompetitionDTO dto) {

        CompetitionType competitionType = competitionTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new CompetitionTypeNotFoundException("Competition type not found with id: " + dto.getTypeId()));

        return Competition.builder()
                .competitionId(dto.getCompetitionId())
                .competitionType(competitionType)
                .matchDurationMinutes(dto.getMatchDurationMinutes())
                .availableCourts(dto.getAvailableCourts())
                .participantsLimit(dto.getParticipantsLimit())
                .streetAddress(dto.getStreetAddress())
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .registrationOpen(dto.getRegistrationOpen())
                .build();
    }

}
