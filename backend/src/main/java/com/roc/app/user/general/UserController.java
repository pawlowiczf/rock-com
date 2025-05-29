package com.roc.app.user.general;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/id/{email}")
    public ResponseEntity<Long> getUserIdByEmail(@PathVariable String email) {
        try {
            Long userId = userService.getUserIdByEmail(email);
            return ResponseEntity.ok(userId);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/authorities/{userMail}")
    public ResponseEntity<String> checkPermissions(@PathVariable String userMail) {
        try {
            return ResponseEntity.ok( userService.getUserRole(userService.getUserIdByEmail(userMail)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
