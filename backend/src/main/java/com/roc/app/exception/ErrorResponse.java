package com.roc.app.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public static void writeToResponse(HttpServletResponse response, HttpStatus status, String error, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                error,
                message,
                LocalDateTime.now()
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
