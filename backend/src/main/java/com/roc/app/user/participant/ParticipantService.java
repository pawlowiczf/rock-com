package com.roc.app.user.participant;

import com.roc.app.user.participant.dto.ParticipantCreateRequestDto;
import com.roc.app.user.participant.dto.ParticipantResponseDto;
import com.roc.app.user.participant.exception.ParticipantNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;

    public ParticipantService(ParticipantRepository participantRepository, PasswordEncoder passwordEncoder) {
        this.participantRepository = participantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Participant> findAll() {
        return participantRepository.findAll();
    }

    public ParticipantResponseDto findById(Long id) {
        Participant participant = participantRepository.findById(id).orElseThrow(() -> new ParticipantNotFoundException(id));

        return ParticipantResponseDto.fromModel(participant);
    }

    @Transactional
    public ParticipantResponseDto createParticipant(ParticipantCreateRequestDto requestDto) {
        Participant participant = Participant.builder()
                .firstName(requestDto.firstName())
                .lastName(requestDto.lastName())
                .email(requestDto.email())
                .password(passwordEncoder.encode(requestDto.password()))
                .city(requestDto.city())
                .phoneNumber(requestDto.phoneNumber())
                .birthDate(requestDto.birthDate())
                .build();

        Participant savedParticipant = participantRepository.save(participant);
        return ParticipantResponseDto.fromModel(savedParticipant);
    }
}
