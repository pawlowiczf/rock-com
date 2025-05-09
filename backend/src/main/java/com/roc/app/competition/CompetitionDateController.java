package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionDateCreateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
