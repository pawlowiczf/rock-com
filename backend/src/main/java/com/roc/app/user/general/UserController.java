package com.roc.app.user.general;

import com.roc.app.match.UserMatchService;
import com.roc.app.match.dto.UserMatchDto;
import com.roc.app.user.general.dto.UserCreateRequestDto;
import com.roc.app.user.general.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserMatchService matchService;

    public UserController(UserService userService, UserMatchService matchService) {
        this.userService = userService;
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto requestDto) {
        /*
        Not sure whether we want to keep this endpoint or not, or even the whole controller.
         */
        UserResponseDto user = userService.createUser(requestDto);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/matches")
    public List<UserMatchDto> getUserMatches(@PathVariable Integer userId) {
        return matchService.getUserMatches(userId);
    }
}
