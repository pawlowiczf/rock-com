package com.roc.app.user.general;

import com.roc.app.user.general.dto.UserResponseDto;
import com.roc.app.user.general.dto.UserUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id")
    public ResponseEntity<Integer> getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user.getUserId());
    }

    @GetMapping("/authorities")
    public ResponseEntity<String> checkPermissions(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok( userService.getUserRole(user.getUserId()));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserById(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(UserResponseDto.fromModel(user));
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<UserResponseDto> updateUserProfile(@RequestBody UserUpdateRequestDto updateDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserResponseDto responseDto = userService.updateUser(user, updateDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
