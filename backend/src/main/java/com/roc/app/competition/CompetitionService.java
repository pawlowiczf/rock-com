package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionResponseDto;
import com.roc.app.competition.dto.CompetitionCreateRequestDto;
import com.roc.app.competition.dto.UpcomingCompetitionDto;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import com.roc.app.competition.exception.CompetitionTypeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final CompetitionDateRepository competitionDateRepository;
    private final CompetitionMapper competitionMapper;

    public List<CompetitionResponseDto> getAllCompetitions() {
        return competitionRepository.findAll().stream()
                .map(competitionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> getCompetitionsByType(String competitionType) {
        CompetitionType competitionTypeEnum;
        try{
            competitionTypeEnum = CompetitionType.valueOf(competitionType);
        }catch (IllegalArgumentException e){
            throw new CompetitionTypeNotFoundException(competitionType);
        }

        return competitionRepository.findByType(competitionTypeEnum).stream()
                .map(competitionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> getOpenCompetitions() {
        return competitionRepository.findByRegistrationOpen(true).stream()
                .map(competitionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> getCompetitionsByCity(String city) {
        return competitionRepository.findByCity(city).stream()
                .map(competitionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public CompetitionResponseDto getCompetitionById(Integer id) {
        return competitionRepository.findById(id)
                .map(competitionMapper::mapToDto)
                .orElseThrow(() -> new CompetitionNotFoundException(id));
    }

    @Transactional
    public CompetitionResponseDto createCompetition(CompetitionCreateRequestDto competitionDTO) {
        Competition competition = competitionMapper.mapToEntity(competitionDTO);
        Competition savedCompetition = competitionRepository.save(competition);
        return competitionMapper.mapToDto(savedCompetition);
    }


    @Transactional
    public CompetitionResponseDto updateCompetition(Integer id, CompetitionResponseDto competitionDTO) {
        Competition competition = competitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(id));

        competition.setType(competitionDTO.type());
        competition.setMatchDurationMinutes(competitionDTO.matchDurationMinutes());
        competition.setAvailableCourts(competitionDTO.availableCourts());
        competition.setParticipantsLimit(competitionDTO.participantsLimit());
        competition.setStreetAddress(competitionDTO.streetAddress());
        competition.setCity(competitionDTO.city());
        competition.setPostalCode(competitionDTO.postalCode());
        competition.setRegistrationOpen(competitionDTO.registrationOpen());

        Competition updatedCompetition = competitionRepository.save(competition);
        return competitionMapper.mapToDto(updatedCompetition);
    }



    @Transactional
    public void deleteCompetition(Integer id) {
        if (!competitionRepository.existsById(id)) {
            throw new CompetitionNotFoundException(id);
        }
        competitionRepository.deleteById(id);
    }
    public List<UpcomingCompetitionDto> getUpcomingCompetitions() {
        return competitionDateRepository.findUpcomingCompetitions();
    }
}
