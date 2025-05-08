package com.roc.app.user.referee.general;

import com.roc.app.user.referee.exception.RefereeNotFoundException;
import com.roc.app.user.referee.general.dto.RefereeCreateRequestDto;
import com.roc.app.user.referee.general.dto.RefereeResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/referee")
public class RefereeController {
    private final RefereeService refereeService;

    public RefereeController(RefereeService refereeService) {
        this.refereeService = refereeService;
    }

    @PostMapping
    public ResponseEntity<RefereeResponseDto> addReferee(@RequestBody @Valid RefereeCreateRequestDto requestDto) {
        RefereeResponseDto referee = refereeService.createReferee(requestDto);
        return ResponseEntity.ok(referee);
    }
}
