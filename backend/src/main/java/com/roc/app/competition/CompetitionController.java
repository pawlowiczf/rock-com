package com.roc.app.competition;

import com.roc.app.competition.dto.UpcomingCompetitionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping("/upcoming")
    public List<UpcomingCompetitionDto> getUpcomingCompetitions() {
        return competitionService.getUpcomingCompetitions();
    }
}
