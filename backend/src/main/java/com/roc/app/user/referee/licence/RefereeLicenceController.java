package com.roc.app.user.referee.licence;

import com.roc.app.user.referee.exception.RefereeNotFoundException;
import com.roc.app.user.referee.general.dto.RefereeResponseDto;
import com.roc.app.user.referee.licence.dto.RefereeAddLicenceRequestDto;
import com.roc.app.user.referee.licence.dto.RefereeAddLicenceResponseDto;
import com.roc.app.user.referee.licence.dto.RefereeLicenceResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licence")
public class RefereeLicenceController {
    private final RefereeLicenceService refereeLicenceService;

    public RefereeLicenceController(RefereeLicenceService refereeLicenceService) {
        this.refereeLicenceService = refereeLicenceService;
    }

    @PostMapping
    public ResponseEntity<?> addRefereeLicence(
            @RequestBody @Valid RefereeAddLicenceRequestDto refereeAddLicenceRequestDto) {

        RefereeAddLicenceResponseDto response;
        try {
            response = refereeLicenceService.addRefereeLicence(refereeAddLicenceRequestDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-referee/{refereeId}")
    public ResponseEntity<List<RefereeLicenceResponseDto>> getRefereeLicences(@PathVariable long refereeId) {
        List<RefereeLicenceResponseDto> licences = refereeLicenceService.getRefereeLicencesByRefereeId(refereeId);
        return ResponseEntity.ok(licences);
    }

    @GetMapping("/by-license/{license}")
    public ResponseEntity<RefereeLicenceResponseDto> getRefereeLicenceByLicense(@PathVariable String license) {
        RefereeLicenceResponseDto licence = refereeLicenceService.getRefereeLicenceByLicense(license);
        return ResponseEntity.ok(licence);
    }
}
