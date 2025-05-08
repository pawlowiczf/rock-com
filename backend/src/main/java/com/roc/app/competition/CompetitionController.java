package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionResponseDto;
import com.roc.app.competition.dto.CompetitionCreateRequestDto;
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
    public ResponseEntity<List<CompetitionResponseDto>> getAllCompetitions() {
        List<CompetitionResponseDto> competitions = competitionService.getAllCompetitions();
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<CompetitionResponseDto>> getCompetitionsByType(@PathVariable String competitionType) {
        List<CompetitionResponseDto> competitions = competitionService.getCompetitionsByType(competitionType);
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/open")
    public ResponseEntity<List<CompetitionResponseDto>> getOpenCompetitions() {
        List<CompetitionResponseDto> competitions = competitionService.getOpenCompetitions();
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<CompetitionResponseDto>> getCompetitionsByCity(@PathVariable String city) {
        List<CompetitionResponseDto> competitions = competitionService.getCompetitionsByCity(city);
        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionResponseDto> getCompetitionById(@PathVariable Integer id) {
        CompetitionResponseDto competition = competitionService.getCompetitionById(id);
        return ResponseEntity.ok(competition);
    }

    @PostMapping
    public ResponseEntity<CompetitionResponseDto> createCompetition(@Valid @RequestBody CompetitionCreateRequestDto competitionDTO) {
        CompetitionResponseDto createdCompetition = competitionService.createCompetition(competitionDTO);
        return new ResponseEntity<>(createdCompetition, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionResponseDto> updateCompetition(
            @PathVariable Integer id,
            @Valid @RequestBody CompetitionResponseDto competitionDTO) {
        CompetitionResponseDto updatedCompetition = competitionService.updateCompetition(id, competitionDTO);
        return ResponseEntity.ok(updatedCompetition);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetition(@PathVariable Integer id) {
        competitionService.deleteCompetition(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/upcoming")
    public List<UpcomingCompetitionDto> getUpcomingCompetitions() {
        return competitionService.getUpcomingCompetitions();
    }
}

