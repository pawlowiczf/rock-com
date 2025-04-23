package com.roc.app.competitionType;

import com.roc.app.competitionType.dto.CompetitionTypeDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competition-types")
@RequiredArgsConstructor
public class CompetitionTypeController {

    private final CompetitionTypeService competitionTypeService;

    @GetMapping
    public ResponseEntity<List<CompetitionTypeDTO>> getAllCompetitionTypes() {
        List<CompetitionTypeDTO> competitionTypes = competitionTypeService.getAllCompetitionTypes();
        return ResponseEntity.ok(competitionTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionTypeDTO> getCompetitionTypeById(@PathVariable Integer id) {
        CompetitionTypeDTO competitionType = competitionTypeService.getCompetitionTypeById(id);
        return ResponseEntity.ok(competitionType);
    }

    @PostMapping
    public ResponseEntity<CompetitionTypeDTO> createCompetitionType(@Valid @RequestBody CompetitionTypeDTO competitionTypeDTO) {
        CompetitionTypeDTO createdCompetitionType = competitionTypeService.createCompetitionType(competitionTypeDTO);
        return new ResponseEntity<>(createdCompetitionType, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionTypeDTO> updateCompetitionType(
            @PathVariable Integer id,
            @Valid @RequestBody CompetitionTypeDTO competitionTypeDTO) {
        CompetitionTypeDTO updatedCompetitionType = competitionTypeService.updateCompetitionType(id, competitionTypeDTO);
        return ResponseEntity.ok(updatedCompetitionType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetitionType(@PathVariable Integer id) {
        competitionTypeService.deleteCompetitionType(id);
        return ResponseEntity.noContent().build();
    }
}
