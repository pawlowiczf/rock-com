package com.roc.app.user.general;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
}
