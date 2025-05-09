package com.roc.app.user.referee.licence;

import com.roc.app.user.referee.licence.dto.RefereeCreateLicenceRequestDto;
import com.roc.app.user.referee.licence.dto.RefereeCreateLicenceResponseDto;
import com.roc.app.user.referee.licence.dto.RefereeLicenceResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/licences")
public class RefereeLicenceController {
    private final RefereeLicenceService refereeLicenceService;

    public RefereeLicenceController(RefereeLicenceService refereeLicenceService) {
        this.refereeLicenceService = refereeLicenceService;
    }

    @PostMapping
    public ResponseEntity<?> addRefereeLicence(
            @RequestBody @Valid RefereeCreateLicenceRequestDto refereeAddLicenceRequestDto) {

        RefereeCreateLicenceResponseDto response;
        try {
            response = refereeLicenceService.addRefereeLicence(refereeAddLicenceRequestDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{license}")
    public ResponseEntity<RefereeLicenceResponseDto> getRefereeLicenceByLicense(@PathVariable String license) {
        RefereeLicenceResponseDto licence = refereeLicenceService.getRefereeLicenceByLicense(license);
        return ResponseEntity.ok(licence);
    }
}
