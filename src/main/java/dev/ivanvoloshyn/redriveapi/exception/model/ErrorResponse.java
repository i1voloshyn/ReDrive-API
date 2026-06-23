package dev.ivanvoloshyn.redriveapi.exception.model;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        int code,
        String path,
        String message,
        Map<String, String> errors) {

    public static ErrorResponse create(Instant timestamp,
                                       HttpStatus status,
                                       String path,
                                       String message,
                                       Map<String, String> errors) {
        return new ErrorResponse(timestamp,status.value(), path, message, errors);
    }

}
