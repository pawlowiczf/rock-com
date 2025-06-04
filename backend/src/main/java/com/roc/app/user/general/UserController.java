package com.roc.app.user.general;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.roc.app.user.general.exception.UserNotFoundException;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user.getUserId());
    }

    @GetMapping("/authorities")
    public ResponseEntity<String> checkPermissions(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok( userService.getUserRole(user.getUserId()));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserById(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getUserId();

        if (userId == null) {
            throw new UserNotFoundException(userId);
        }

        User foundUser = userService.getUserByUserId(userId);
        return ResponseEntity.ok(foundUser);
    }

    // Zapraszam backend do zabawy
    @PatchMapping("/updateProfile")
    public ResponseEntity<User> updateUserProfile(Authentication authentication, User updatedUser) {
//        User user = (User) authentication.getPrincipal();
//        Long userId = user.getUserId();
//
//        if (userId == null) {
//            throw new UserNotFoundException(userId);
//        }
//
//        User existingUser = userService.getUserByUserId(userId);
//        existingUser.updateProfile(updatedUser);
//        User savedUser = userService.save(existingUser);
//
//        return ResponseEntity.status(HttpStatus.OK).body(savedUser);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
