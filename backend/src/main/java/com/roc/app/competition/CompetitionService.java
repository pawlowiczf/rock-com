package com.roc.app.competition;

import com.roc.app.competition.dto.UpcomingCompetitionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionService {

    private final CompetitionDateRepository competitionDateRepository;

    public CompetitionService(CompetitionDateRepository competitionDateRepository) {
        this.competitionDateRepository = competitionDateRepository;
    }

    public List<UpcomingCompetitionDto> getUpcomingCompetitions() {
        return competitionDateRepository.findUpcomingCompetitions();
    }
}
