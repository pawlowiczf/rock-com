package com.roc.app.user.participant;


import com.roc.app.user.participant.dto.ParticipantCreateRequestDto;
import com.roc.app.user.participant.dto.ParticipantResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/participant")
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping
    public ResponseEntity<ParticipantResponseDto> createParticipant(@RequestBody ParticipantCreateRequestDto requestDto) {
        ParticipantResponseDto participant = participantService.create(requestDto);
        return ResponseEntity.ok(participant);
    }
}
