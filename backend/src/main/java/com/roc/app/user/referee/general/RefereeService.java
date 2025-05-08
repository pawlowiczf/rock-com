package com.roc.app.user.referee.general;

import com.roc.app.user.general.UserService;
import com.roc.app.user.general.dto.UserCreateRequestDto;
import com.roc.app.user.general.dto.UserResponseDto;
import com.roc.app.user.participant.exception.ParticipantNotFoundException;
import com.roc.app.user.referee.exception.RefereeNotFoundException;
import com.roc.app.user.general.User;
import com.roc.app.user.general.UserRepository;
import com.roc.app.user.referee.general.dto.RefereeCreateRequestDto;
import com.roc.app.user.referee.general.dto.RefereeResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RefereeService {
    private final RefereeRepository refereeRepository;
    private final UserService userService;

    public RefereeService(RefereeRepository refereeRepository, UserService userService) {
        this.refereeRepository = refereeRepository;
        this.userService = userService;
    }

    @Transactional
    public RefereeResponseDto createReferee(RefereeCreateRequestDto requestDto) {
        UserCreateRequestDto userCreateRequestDto = requestDto.toUserCreateRequestDto();
        UserResponseDto userResponseDto = userService.createUser(userCreateRequestDto);

        User user = userService.getUserByUserId(userResponseDto.userId());
        Referee referee = Referee.builder()
                .userDetails(user)
                .build();

        refereeRepository.save(referee);
        return RefereeResponseDto.fromModel(referee);
    }

    public Referee getRefereeByRefereeId(Long id) {
        return refereeRepository.findById(id)
                .orElseThrow(() -> new RefereeNotFoundException(id));
    }
}
