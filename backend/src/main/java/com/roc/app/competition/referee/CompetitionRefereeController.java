package com.roc.app.competition.referee;

import com.roc.app.competition.Competition;
import com.roc.app.competition.CompetitionService;
import com.roc.app.competition.referee.dto.CompetitionRefereeListResponseDto;
import com.roc.app.competition.referee.dto.CompetitionRefereeResponseDto;
import com.roc.app.user.referee.general.Referee;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionRefereeController {
    private final CompetitionRefereeService competitionRefereeService;
    private final CompetitionService competitionService;

    public CompetitionRefereeController(CompetitionRefereeService competitionRefereeService, CompetitionService competitionService) {
        this.competitionRefereeService = competitionRefereeService;
        this.competitionService = competitionService;
    }

    @PostMapping("{id}/referee")
    ResponseEntity<CompetitionRefereeResponseDto> assignRefereeToCompetition(@PathVariable Integer id, Authentication authentication) {

        Referee referee = (Referee) authentication.getPrincipal();
        Competition competition = competitionService.getCompetitionById(id).toModel();

        CompetitionRefereeResponseDto response = competitionRefereeService.assignRefereeToCompetition(competition, referee);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/referees")
    ResponseEntity<CompetitionRefereeListResponseDto> getCompetitionReferees(@PathVariable Integer id, Authentication authentication) {
        Competition competition = competitionService.getCompetitionById(id).toModel();

        List<CompetitionRefereeResponseDto> referees = competitionRefereeService.listCompetitionReferees(competition);
        CompetitionRefereeListResponseDto response = new CompetitionRefereeListResponseDto(referees);
        return ResponseEntity.ok(response);
    }
}
