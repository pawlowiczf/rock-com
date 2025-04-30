package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionDTO;
import com.roc.app.competition.dto.CreateCompetitionDto;
import com.roc.app.competition.dto.UpcomingCompetitionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;

    @GetMapping
    public ResponseEntity<List<CompetitionDTO>> getAllCompetitions() {
        List<CompetitionDTO> competitions = competitionService.getAllCompetitions();
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<CompetitionDTO>> getCompetitionsByType(@PathVariable String competitionType) {
        List<CompetitionDTO> competitions = competitionService.getCompetitionsByType(competitionType);
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/open")
    public ResponseEntity<List<CompetitionDTO>> getOpenCompetitions() {
        List<CompetitionDTO> competitions = competitionService.getOpenCompetitions();
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<CompetitionDTO>> getCompetitionsByCity(@PathVariable String city) {
        List<CompetitionDTO> competitions = competitionService.getCompetitionsByCity(city);
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable Long id) {
        CompetitionDTO competition = competitionService.getCompetitionById(id);
        return ResponseEntity.ok(competition);
    }

    @PostMapping
    public ResponseEntity<CompetitionDTO> createCompetition(@Valid @RequestBody CreateCompetitionDto competitionDTO) {
        CompetitionDTO createdCompetition = competitionService.createCompetition(competitionDTO);
        return new ResponseEntity<>(createdCompetition, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionDTO> updateCompetition(
            @PathVariable Long id,
            @Valid @RequestBody CompetitionDTO competitionDTO) {
        CompetitionDTO updatedCompetition = competitionService.updateCompetition(id, competitionDTO);
        return ResponseEntity.ok(updatedCompetition);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetition(@PathVariable Long id) {
        competitionService.deleteCompetition(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/upcoming")
    public List<UpcomingCompetitionDto> getUpcomingCompetitions() {
        return competitionService.getUpcomingCompetitions();
    }
}

