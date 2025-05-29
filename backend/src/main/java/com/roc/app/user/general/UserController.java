package com.roc.app.user.general;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/authorities/{userMail}")
    public ResponseEntity<String> checkPermissions(@PathVariable String userMail) {
        System.out.println("Checking permissions for user: " + userMail);
        Long userId = userService.getUserIdByEmail(userMail);
        System.out.println("User ID: " + userId);
        String userRole = userService.getUserRole(userId);
        System.out.println("User Role: " + userRole);
        return ResponseEntity.ok(userService.getUserRole(userService.getUserIdByEmail(userMail)));

    }
}
