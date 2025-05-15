package com.roc.app.user.general;

import com.roc.app.match.MatchService;
import com.roc.app.match.dto.ParticipantMatchResponseDto;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto requestDto) {
        /*
        Not sure whether we want to keep this endpoint or not, or even the whole controller.
         */
        UserResponseDto user = userService.createUser(requestDto);

        return ResponseEntity.ok(user);
    }

}
