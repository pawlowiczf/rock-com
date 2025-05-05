package com.roc.app.referee.general;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RefereeService {
    private final RefereeRepository refereeRepository;

    public RefereeService(RefereeRepository refereeRepository) {
        this.refereeRepository = refereeRepository;
    }

    @Transactional
    public void addReferee(Referee referee) {
        refereeRepository.save(referee);
    }
}
