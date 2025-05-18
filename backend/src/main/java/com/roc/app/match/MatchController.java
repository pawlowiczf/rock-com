package com.roc.app.match;

import com.roc.app.match.dto.MatchCreateRequestDto;
import com.roc.app.match.dto.MatchUpdateRequestDto;
import com.roc.app.match.dto.RefereeMatchResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<?> createMatch(@Valid @RequestBody MatchCreateRequestDto requestDto) {
        Integer createdMatchId = matchService.createMatch(requestDto);
        return ResponseEntity.ok("Match created with ID: " + createdMatchId);
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<?> updateMatch(@PathVariable Integer matchId,
                                         @Valid @RequestBody MatchUpdateRequestDto requestDto) {
        matchService.updateMatch(matchId, requestDto);
        return ResponseEntity.ok("Match updated successfully.");
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<?> deleteMatch(@PathVariable Integer matchId) {
        matchService.deleteMatch(matchId);
        return ResponseEntity.ok("Match deleted successfully.");
    }

}
