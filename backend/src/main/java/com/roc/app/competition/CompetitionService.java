package com.roc.app.competition;

import com.roc.app.bracket.BracketService;
import com.roc.app.competition.assignment.CompetitionParticipant;
import com.roc.app.competition.assignment.CompetitionParticipantRepository;
import com.roc.app.competition.dto.CompetitionResponseDto;
import com.roc.app.competition.dto.CompetitionCreateRequestDto;
import com.roc.app.competition.dto.UpcomingCompetitionDto;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import com.roc.app.competition.exception.CompetitionTypeNotFoundException;
import com.roc.app.match.Match;
import com.roc.app.match.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionParticipantRepository competitionParticipantRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionDateRepository competitionDateRepository;
    private final MatchService matchService;
    private final BracketService bracketService;
    private final CompetitionDateService competitionDateService;
    private final Random random = new Random();

    public List<CompetitionResponseDto> getAllCompetitions() {
        return competitionRepository.findAll().stream()
                .map(CompetitionResponseDto::fromModel)
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

        competition.setName(competitionDTO.name());
        competition.setType(competitionDTO.type());
        competition.setMatchDurationMinutes(competitionDTO.matchDurationMinutes());
        competition.setAvailableCourts(competitionDTO.availableCourts());
        competition.setParticipantsLimit(competitionDTO.participantsLimit());
        competition.setStreetAddress(competitionDTO.streetAddress());
        competition.setCity(competitionDTO.city());
        competition.setPostalCode(competitionDTO.postalCode());
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
        int rounds = 0;
        long usedMinutes = 0;
        long totalAvailableMinutes = competitionDateService.getTotalCompetitionDurationMinutes(competition);

        while (true) {
            int matchesInRound = (int) Math.pow(2, rounds);
            int batches = (int) Math.ceil((double) matchesInRound / competition.getAvailableCourts()); // TODO: change available courts to min from them and number of referees
            int roundDuration = batches * competition.getMatchDurationMinutes();

            if (usedMinutes + roundDuration > totalAvailableMinutes) {
                break;
            }

            usedMinutes += roundDuration;
            rounds++;
        }

        int participantsLimit = (int) Math.pow(2, rounds-1);
        competition.setRegistrationOpen(true);
        competition.setParticipantsLimit(participantsLimit);
        competitionRepository.save(competition);
        return participantsLimit;
    }

    public void startCompetition(Integer id) {
        // TODO: add referee assignment after implementing referee assignment
        Competition competition = competitionRepository.findById(id)
                .orElseThrow(() -> new CompetitionNotFoundException(id));

        competition.setRegistrationOpen(false);
        competitionRepository.save(competition);

        List<CompetitionParticipant> participants = competitionParticipantRepository.findByCompetitionId(id);
        int actualParticipants = participants.size();
        int maxParticipants = adjustParticipantsLimit(competition, actualParticipants);
        int byeCount = maxParticipants - actualParticipants;
        int scheduledPlayers = actualParticipants - byeCount;

        TimeSlots slots = competitionDateService.generateTimeSlots(competition);
        List<Match> match = createFirstRound(competition, participants, byeCount, scheduledPlayers, slots);
        createNextRounds(competition, match, slots);
        matchService.updateMatchesFollowingByeMatches(competition.getCompetitionId());
    }

    private int adjustParticipantsLimit(Competition competition, int actualParticipants) {
        int size = 1;
        while (size < actualParticipants) {
            size <<= 1;
        }
        int newLimit = Math.min(size, competition.getParticipantsLimit());
        competition.setParticipantsLimit(newLimit);
        return newLimit;
    }

    private List<Match> createFirstRound(Competition competition, List<CompetitionParticipant> participants, int byeCount, int scheduledPlayers, TimeSlots slots) {
        List<Match> matches = new LinkedList<>();
        for (int i = 0; i < byeCount; i++) {
            matches.add(matchService.createByeMatch(competition, drawRandomPlayerId(participants)));
        }

        for (int i = 0; i < scheduledPlayers / 2; i++) {
            Integer player1 = drawRandomPlayerId(participants);
            Integer player2 = drawRandomPlayerId(participants);
            matches.add(matchService.createScheduledMatch(competition, player1, player2, null, slots.getNext()));
        }
        slots.removeSlot(matches.getLast().getMatchDate());
        return matches;
    }

    private void createNextRounds(Competition competition, List<Match> matches, TimeSlots slots) {
        List<Match> nextMatches = new LinkedList<>();
        while(matches.size() > 1) {
            for(int i = 0; i < matches.size()/2 ; i++) {
                nextMatches.add(matchService.createScheduledMatch(competition, null, null, null, slots.getNext()));
            }
            slots.removeSlot(nextMatches.getLast().getMatchDate());
            bracketService.saveRound(matches, nextMatches);
            matches = nextMatches;
            nextMatches = new LinkedList<>();
        }
    }

    private Integer drawRandomPlayerId(List<CompetitionParticipant> participants) {
        return participants.remove(random.nextInt(participants.size())).getParticipantId();
    }
}
