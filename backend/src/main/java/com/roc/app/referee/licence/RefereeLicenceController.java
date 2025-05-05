package com.roc.app.referee.licence;

import com.roc.app.referee.exception.RefereeNotFoundException;
import com.roc.app.referee.licence.dto.RefereeVerifyLicenceRequestDto;
import com.roc.app.referee.licence.dto.RefereeVerifyLicenceResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/referee/licence")
public class RefereeLicenceController {
    private final RefereeLicenceService refereeLicenceService;

    public RefereeLicenceController(RefereeLicenceService refereeLicenceService) {
        this.refereeLicenceService = refereeLicenceService;
    }

    @PostMapping
    public ResponseEntity<?> verifyRefereeLicence(
            @RequestBody RefereeVerifyLicenceRequestDto refereeVerifyLicenceRequestDto) {

        RefereeVerifyLicenceResponseDto response;
        try {
            response = refereeLicenceService.verifyRefereeLicence(refereeVerifyLicenceRequestDto);

        } catch (RefereeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
