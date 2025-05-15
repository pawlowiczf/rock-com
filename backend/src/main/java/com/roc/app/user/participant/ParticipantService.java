package com.roc.app.user.participant;

import com.roc.app.match.MatchService;
import com.roc.app.match.dto.ParticipantMatchResponseDto;
import com.roc.app.user.general.User;
import com.roc.app.user.general.UserService;
import com.roc.app.user.general.dto.UserCreateRequestDto;
import com.roc.app.user.general.dto.UserResponseDto;
import com.roc.app.user.participant.dto.ParticipantCreateRequestDto;
import com.roc.app.user.participant.dto.ParticipantResponseDto;
import com.roc.app.user.participant.exception.ParticipantNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserService userService;

    public ParticipantService(ParticipantRepository participantRepository, UserService userService) {
        this.participantRepository = participantRepository;
        this.userService = userService;
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
        UserCreateRequestDto userCreateRequestDto = requestDto.toUserCreateRequestDto();
        UserResponseDto userResponseDto = userService.createUser(userCreateRequestDto);

        User user = userService.getUserByUserId(userResponseDto.userId());

        Participant participant = Participant.builder()
                .userDetails(user)
                .birthDate(requestDto.birthDate())
                .build();

        participantRepository.save(participant);

        return ParticipantResponseDto.fromModel(participant);
    }

}
