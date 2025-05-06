package com.roc.app.user.referee.licence;

import com.roc.app.user.referee.exception.RefereeNotFoundException;
import com.roc.app.user.referee.licence.dto.RefereeAddLicenceRequestDto;
import com.roc.app.user.referee.licence.dto.RefereeAddLicenceResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addRefereeLicence(
            @RequestBody RefereeAddLicenceRequestDto refereeAddLicenceRequestDto) {

        RefereeAddLicenceResponseDto response;
        try {
            response = refereeLicenceService.addRefereeLicence(refereeAddLicenceRequestDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
