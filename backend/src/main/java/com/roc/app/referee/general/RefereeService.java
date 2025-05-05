package com.roc.app.referee.general;

import org.springframework.stereotype.Service;

@Service
public class RefereeService {
    private final RefereeRepository refereeRepository;

    public RefereeService(RefereeRepository refereeRepository) {
        this.refereeRepository = refereeRepository;
    }


}
