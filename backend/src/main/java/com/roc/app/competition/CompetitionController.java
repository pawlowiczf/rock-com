package com.roc.app.competition;

import com.roc.app.competition.assignment.CompetitionParticipantService;
import com.roc.app.competition.assignment.dto.CompetitionParticipantResponseDto;
import com.roc.app.competition.assignment.dto.CompetitionParticipantsListResponseDto;
import com.roc.app.competition.dto.CompetitionDateResponseDto;
import com.roc.app.competition.dto.CompetitionResponseDto;
import com.roc.app.competition.dto.CompetitionCreateRequestDto;
import com.roc.app.competition.dto.UpcomingCompetitionDto;
import com.roc.app.match.MatchStatus;
import com.roc.app.match.dto.MatchResponseDto;
import com.roc.app.user.participant.Participant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;
    private final CompetitionParticipantService competitionParticipantService;
    private final CompetitionDateService competitionDateService;

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

    @PostMapping("/{id}/enroll")
    public ResponseEntity<CompetitionParticipantResponseDto> enrollIntoCompetition(@PathVariable Integer id, Authentication authentication) {
        Participant participant = (Participant) authentication.getPrincipal();
        Competition competition = competitionService.getCompetitionById(id).toModel();

        CompetitionParticipantResponseDto responseDto = competitionParticipantService.enroll(competition, participant);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("{id}/participants")
    public ResponseEntity<CompetitionParticipantsListResponseDto> getCompetitionParticipants(@PathVariable Integer id) {
        CompetitionParticipantsListResponseDto participants = competitionParticipantService.listCompetitionParticipants(id);
        return ResponseEntity.ok(participants);
    }
      
    @GetMapping("/{id}/dates")
    public ResponseEntity<List<CompetitionDateResponseDto>> getCompetitionDates(@PathVariable Integer id) {
        List<CompetitionDateResponseDto> competitionDateResponseDtoList = competitionDateService.getCompetitionDates(id);
        return ResponseEntity.ok(competitionDateResponseDtoList);
    }

}

