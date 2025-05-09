package com.roc.app.user.referee.general;

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

    public RefereeController(RefereeService refereeService, RefereeLicenceService refereeLicenceService) {
        this.refereeService = refereeService;
        this.refereeLicenceService = refereeLicenceService;
    }

    @PostMapping
    public ResponseEntity<RefereeResponseDto> addReferee(@RequestBody @Valid RefereeCreateRequestDto requestDto) {
        RefereeResponseDto referee = refereeService.createReferee(requestDto);
        return ResponseEntity.ok(referee);
    }

    @GetMapping("/{refereeId}/licences")
    public ResponseEntity<List<RefereeLicenceResponseDto>> getRefereeLicences(@PathVariable long refereeId) {
        List<RefereeLicenceResponseDto> licences = refereeLicenceService.getRefereeLicencesByRefereeId(refereeId);
        return ResponseEntity.ok(licences);
    }
}
