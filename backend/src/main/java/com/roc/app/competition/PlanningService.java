package com.roc.app.competition;

import com.roc.app.bracket.BracketService;
import com.roc.app.competition.assignment.CompetitionParticipant;
import com.roc.app.competition.dto.CompetitionDateResponseDto;
import com.roc.app.match.Match;
import com.roc.app.match.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PlanningService {

    private final CompetitionDateService dateService;
    private final BracketService bracketService;
    private final MatchService matchService;
    private final Random random = new Random();

    public int calculateParticipantsLimit(Competition competition){
        int rounds = 0;
        long usedMinutes = 0;
        long totalAvailableMinutes = dateService.getTotalCompetitionDurationMinutes(competition);

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

        return (int) Math.pow(2, rounds-1);
    }

    public int getAdjustedParticipantsLimit(Competition competition, int actualParticipants) {
        int size = 1;
        while (size < actualParticipants) {
            size <<= 1;
        }
        return Math.min(size, competition.getParticipantsLimit());
    }

    public TimeSlots generateTimeSlots(Competition competition) {
        List<CompetitionDateResponseDto> dates = dateService.getCompetitionDates(competition.getCompetitionId());
        return new TimeSlots(competition, dates);
    }

    public List<Match> createFirstRound(Competition competition, List<CompetitionParticipant> participants, int byeCount, int scheduledPlayers, TimeSlots slots) {
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

    public void createNextRounds(Competition competition, List<Match> matches, TimeSlots slots) {
        List<Match> nextMatches = new LinkedList<>();
        while (matches.size() > 1) {
            for (int i = 0; i < matches.size() / 2; i++) {
                nextMatches.add(matchService.createScheduledMatch(competition, null, null, null, slots.getNext()));
            }
            slots.removeSlot(nextMatches.getLast().getMatchDate());
            bracketService.generateBracketConnections(matches, nextMatches);
            matches = nextMatches;
            nextMatches = new LinkedList<>();
        }
    }

    private Integer drawRandomPlayerId(List<CompetitionParticipant> participants) {
        return participants.remove(random.nextInt(participants.size())).getParticipantId();
    }
}
