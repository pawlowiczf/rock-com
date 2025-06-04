package com.roc.app.competition;

import com.roc.app.competition.assignment.CompetitionParticipant;
import com.roc.app.competition.assignment.CompetitionParticipantRepository;
import com.roc.app.competition.dto.CompetitionCreateRequestDto;
import com.roc.app.competition.dto.CompetitionResponseDto;
import com.roc.app.competition.dto.UpcomingCompetitionDto;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import com.roc.app.competition.exception.CompetitionTypeNotFoundException;
import com.roc.app.match.Match;
import com.roc.app.match.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionParticipantRepository competitionParticipantRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionDateRepository competitionDateRepository;
    private final MatchService matchService;
    private final PlanningService planningService;

    public List<CompetitionResponseDto> getAllCompetitions() {
        return competitionRepository.findAll().stream()
                .map(CompetitionResponseDto::fromModel)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> getCompetitionsByType(String competitionType) {
        CompetitionType competitionTypeEnum;
        try {
            competitionTypeEnum = CompetitionType.valueOf(competitionType);
        } catch (IllegalArgumentException e) {
            throw new CompetitionTypeNotFoundException(competitionType);
        }

        return competitionRepository.findByType(competitionTypeEnum).stream()
                .map(CompetitionResponseDto::fromModel)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> getOpenCompetitions() {
        return competitionRepository.findByRegistrationOpen(true).stream()
                .map(CompetitionResponseDto::fromModel)
                .collect(Collectors.toList());
    }

    public List<CompetitionResponseDto> getCompetitionsByCity(String city) {
        return competitionRepository.findByCity(city).stream()
                .map(CompetitionResponseDto::fromModel)
                .collect(Collectors.toList());
    }

    public CompetitionResponseDto getCompetitionById(Integer id) {
        return competitionRepository.findById(id)
                .map(CompetitionResponseDto::fromModel)
                .orElseThrow(() -> new CompetitionNotFoundException(id));
    }

    @Transactional
    public CompetitionResponseDto createCompetition(CompetitionCreateRequestDto competitionDTO) {
        Competition competition = competitionDTO.toModel();
        Competition savedCompetition = competitionRepository.save(competition);
        return CompetitionResponseDto.fromModel(savedCompetition);
    }


    @Transactional
    public CompetitionResponseDto updateCompetition(Integer id, CompetitionResponseDto competitionDTO) {
        Competition competition = competitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(id));

        if (competitionDTO.name() != null) competition.setName(competitionDTO.name());
        if (competitionDTO.type() != null) competition.setType(competitionDTO.type());
        if (competitionDTO.matchDurationMinutes() != null)
            competition.setMatchDurationMinutes(competitionDTO.matchDurationMinutes());
        if (competitionDTO.availableCourts() != null) competition.setAvailableCourts(competitionDTO.availableCourts());
        if (competitionDTO.participantsLimit() != null)
            competition.setParticipantsLimit(competitionDTO.participantsLimit());
        if (competitionDTO.streetAddress() != null) competition.setStreetAddress(competitionDTO.streetAddress());
        if (competitionDTO.city() != null) competition.setCity(competitionDTO.city());
        if (competitionDTO.postalCode() != null) competition.setPostalCode(competitionDTO.postalCode());
        if (competitionDTO.registrationOpen() != null)
            competition.setRegistrationOpen(competitionDTO.registrationOpen());

        Competition updatedCompetition = competitionRepository.save(competition);
        return CompetitionResponseDto.fromModel(updatedCompetition);
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

    public int openRegistrationAndSetParticipantsLimit(Integer competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new CompetitionNotFoundException(competitionId));
        int participantsLimit = planningService.calculateParticipantsLimit(competition);
        competition.setRegistrationOpen(true);
        competition.setParticipantsLimit(participantsLimit);
        competitionRepository.save(competition);
        return participantsLimit;
    }

    public void startCompetition(Integer id) {
        // TODO: add referee assignment after implementing referee assignment
        Competition competition = competitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(id));

        List<CompetitionParticipant> participants = competitionParticipantRepository.findByCompetitionId(id);
        int actualParticipants = participants.size();
        int maxParticipants = planningService.getAdjustedParticipantsLimit(competition, actualParticipants);
        int byeCount = maxParticipants - actualParticipants;
        int scheduledPlayers = actualParticipants - byeCount;

        competition.setRegistrationOpen(false);
        competition.setParticipantsLimit(maxParticipants);
        competitionRepository.save(competition);

        TimeSlots slots = planningService.generateTimeSlots(competition);
        List<Match> match = planningService.createFirstRound(competition, participants, byeCount, scheduledPlayers, slots);
        planningService.createNextRounds(competition, match, slots);
        matchService.updateMatchesFollowingByeMatches(competition.getCompetitionId());
    }
}
