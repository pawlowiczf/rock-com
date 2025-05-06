package com.roc.app.referee.general;

import com.roc.app.referee.exception.RefereeNotFoundException;
import com.roc.app.user.general.User;
import org.springframework.http.HttpStatus;
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

    private record AddRefereeRequest(Long userId) {};
    @PostMapping
    public ResponseEntity<?> addReferee(@RequestBody AddRefereeRequest addRefereeRequest) {
        Long userId = addRefereeRequest.userId();
        Referee referee;

        try {
            referee = refereeService.addReferee(userId);
        } catch (RefereeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(referee.getUser());
    }
}
