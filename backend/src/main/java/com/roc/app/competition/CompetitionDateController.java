package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionDateCreateRequestDto;
import com.roc.app.competition.dto.CompetitionDateUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions-dates")
@RequiredArgsConstructor
public class CompetitionDateController {
    private final CompetitionDateService competitionDateService;

    @PostMapping
    public ResponseEntity<Void> addDatesToCompetition(@Valid @RequestBody List<CompetitionDateCreateRequestDto> competitionDateCreateRequestDto) {
        competitionDateService.addDatesToCompetition(competitionDateCreateRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDate(@PathVariable Integer id, @Valid @RequestBody CompetitionDateUpdateRequestDto competitionDateUpdateRequestDto) {
        competitionDateService.updateDate(id, competitionDateUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDate(@PathVariable Integer id) {
        competitionDateService.deleteDate(id);
        return ResponseEntity.noContent().build();
    }
}
