package com.roc.app.user.referee.general;

import com.roc.app.match.MatchService;
import com.roc.app.match.dto.MatchResponseDto;
import com.roc.app.match.dto.RefereeMatchResponseDto;
import com.roc.app.user.referee.exception.RefereeNotFoundException;
import com.roc.app.user.referee.general.dto.RefereeCreateRequestDto;
import com.roc.app.user.referee.general.dto.RefereeResponseDto;
import com.roc.app.user.referee.licence.RefereeLicenceService;
import com.roc.app.user.referee.licence.dto.RefereeLicenceResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referees")
public class RefereeController {
    private final RefereeService refereeService;
    private final RefereeLicenceService refereeLicenceService;
    private final MatchService matchService;

    public RefereeController(RefereeService refereeService, RefereeLicenceService refereeLicenceService, MatchService matchService) {
        this.refereeService = refereeService;
        this.refereeLicenceService = refereeLicenceService;
        this.matchService = matchService;
    }

    @GetMapping("/{refereeId}")
    public ResponseEntity<RefereeResponseDto> getRefereeById(@PathVariable Integer refereeId) {
        RefereeResponseDto referee = refereeService.getReferee(refereeId);
        return ResponseEntity.ok(referee);
    }

    @PostMapping
    public ResponseEntity<RefereeResponseDto> addReferee(@RequestBody @Valid RefereeCreateRequestDto requestDto) {
        RefereeResponseDto referee = refereeService.createReferee(requestDto);
        return ResponseEntity.ok(referee);
    }

    @GetMapping("/{refereeId}/licences")
    public ResponseEntity<List<RefereeLicenceResponseDto>> getRefereeLicences(@PathVariable Integer refereeId) {
        List<RefereeLicenceResponseDto> licences = refereeLicenceService.getRefereeLicencesByRefereeId(refereeId);
        return ResponseEntity.ok(licences);
    }

    @GetMapping("/{refereeId}/matches")
    public ResponseEntity<List<RefereeMatchResponseDto>> getRefereeMatches(@PathVariable Integer refereeId) {
        return ResponseEntity.ok(matchService.getRefereeMatches(refereeId));
    }
}
