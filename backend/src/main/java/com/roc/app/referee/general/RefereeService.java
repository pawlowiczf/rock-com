package com.roc.app.referee.general;

import com.roc.app.referee.exception.RefereeNotFoundException;
import com.roc.app.user.general.User;
import com.roc.app.user.general.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RefereeService {
    private final RefereeRepository refereeRepository;
    private final UserRepository userRepository;

    public RefereeService(RefereeRepository refereeRepository, UserRepository userRepository) {
        this.refereeRepository = refereeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Referee addReferee(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RefereeNotFoundException("Referee not found. Id: " + userId));

        Referee referee = Referee.builder()
                .user(user)
                .build();
        return refereeRepository.save(referee);
    }
}
