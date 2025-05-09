package com.roc.app.match;

//import com.roc.app.match.dto.MatchCreateRequestDto;
//import com.roc.app.match.dto.MatchUpdateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

// Will have to do match service on a separate branch, so commented out for now.

//    private final MatchService matchService;
//
//    public MatchController(MatchService matchService) {
//        this.matchService = matchService;
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createMatch(@Valid @RequestBody MatchCreateRequestDto requestDto) {
//        Integer createdMatchId = matchService.createMatch(requestDto);
//        return ResponseEntity.ok().body("Match created with ID: " + createdMatchId);
//    }
//
//    @PutMapping("/{matchId}")
//    public ResponseEntity<?> updateMatch(@PathVariable Integer matchId,
//                                         @Valid @RequestBody MatchUpdateRequestDto requestDto) {
//        matchService.updateMatch(matchId, requestDto);
//        return ResponseEntity.ok().body("Match updated successfully.");
//    }
//
//    @DeleteMapping("/{matchId}")
//    public ResponseEntity<?> deleteMatch(@PathVariable Integer matchId) {
//        matchService.deleteMatch(matchId);
//        return ResponseEntity.ok().body("Match deleted successfully.");
//    }
}
