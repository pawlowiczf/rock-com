package com.roc.app.user.referee.general;


import com.roc.app.user.referee.exception.RefereeNotFoundException;
import com.roc.app.user.referee.general.dto.RefereeCreateRequestDto;
import com.roc.app.user.referee.general.dto.RefereeResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RefereeService {
    private final RefereeRepository refereeRepository;
    private final PasswordEncoder passwordEncoder;

    public RefereeService(RefereeRepository refereeRepository, PasswordEncoder passwordEncoder) {
        this.refereeRepository = refereeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RefereeResponseDto createReferee(RefereeCreateRequestDto requestDto) {
        Referee referee = Referee.builder()
                .firstName(requestDto.firstName())
                .lastName(requestDto.lastName())
                .email(requestDto.email())
                .password(passwordEncoder.encode(requestDto.password()))
                .phoneNumber(requestDto.phoneNumber())
                .build();

        refereeRepository.save(referee);
        return RefereeResponseDto.fromModel(referee);
    }

    public Referee getRefereeByRefereeId(Long id) {
        return refereeRepository.findById(id)
                .orElseThrow(() -> new RefereeNotFoundException(id));
    }
}
