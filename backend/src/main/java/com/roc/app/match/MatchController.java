package com.roc.app.match;

import com.roc.app.match.dto.MatchCreateRequestDto;
import com.roc.app.match.dto.MatchResponseDto;
import com.roc.app.match.dto.MatchUpdateRequestDto;
import com.roc.app.match.dto.RefereeMatchResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchResponseDto> getMatchById(@PathVariable Integer matchId) {
        MatchResponseDto match = matchService.getMatchById(matchId);
        return ResponseEntity.ok(match);
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

    // Returns a list of matches during a particular competition, refereed by refereeId
    // /api/matches/competitions/12/referees/9
    @GetMapping("/competitions/{competitionId}/referees/{refereeId}")
    public ResponseEntity<List<MatchResponseDto>> getMatchesByRefereeId(
            @PathVariable(name = "competitionId") Integer competitionId,
            @PathVariable(name = "refereeId") Integer refereeId
    ) {
        List<MatchResponseDto> matches = matchService.getCompetitionMatchesByRefereeId(competitionId, refereeId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/competitions/{competitionId}")
    public ResponseEntity<Map<MatchStatus, List<MatchResponseDto>>> getCompetitionMatchResults(@PathVariable Integer competitionId) {

        Map<MatchStatus, List<MatchResponseDto>> matches = matchService.getMatchesByCompetitionIdGroupedByStatus(competitionId);
        return ResponseEntity.ok(matches);
    }

}
