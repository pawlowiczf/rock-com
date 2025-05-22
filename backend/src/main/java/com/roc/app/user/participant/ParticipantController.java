package com.roc.app.user.participant;


import com.roc.app.match.MatchService;
import com.roc.app.match.dto.ParticipantMatchResponseDto;
import com.roc.app.user.participant.dto.ParticipantCreateRequestDto;
import com.roc.app.user.participant.dto.ParticipantResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantService participantService;
    private final MatchService matchService;

    public ParticipantController(ParticipantService participantService, MatchService matchService) {
        this.participantService = participantService;
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<ParticipantResponseDto> createParticipant(@Valid @RequestBody ParticipantCreateRequestDto requestDto) {
        System.out.println("Creating participant with request: " + requestDto);
        ParticipantResponseDto participant = participantService.createParticipant(requestDto);
        return ResponseEntity.ok(participant);
    }


    @GetMapping("/{userId}/matches")
    public List<ParticipantMatchResponseDto> getParticipantMatches(@PathVariable Integer userId) {
        return matchService.getParticipantMatches(userId);
    }
}
