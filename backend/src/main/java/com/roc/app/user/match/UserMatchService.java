package com.roc.app.user.match;

import com.roc.app.user.match.dto.UserMatchDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMatchService {

    private final UserMatchRepository matchRepository;

    public UserMatchService(UserMatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<UserMatchDto> getUserMatches(Integer userId) {
        return matchRepository.findUserMatches(userId);
    }
}