package com.roc.app.referee.general;

import com.roc.app.user.general.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/referee")
public class RefereeController {
    private final RefereeService refereeService;

    public RefereeController(RefereeService refereeService) {
        this.refereeService = refereeService;
    }

    @PostMapping
    public ResponseEntity<User> addReferee(@RequestBody Referee referee) {
        refereeService.addReferee(referee);
        return ResponseEntity.ok(referee.getUser());
    }
}
