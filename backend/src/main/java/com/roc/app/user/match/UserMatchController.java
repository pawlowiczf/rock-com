package com.roc.app.user.match;

import com.roc.app.user.match.dto.UserMatchDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/matches")
public class UserMatchController {

    private final UserMatchService matchService;

    public UserMatchController(UserMatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<UserMatchDto> getUserMatches(@PathVariable Integer userId) {
        return matchService.getUserMatches(userId);
    }
}