package dev.ivanvoloshyn.redriveapi.exception.model;

import java.util.Map;

public record ErrorResponse(
        int code,
        String path,
        String message,
        Map<String, String> errors) {

    public ErrorResponse(int code, String message, String path) {
        this(code, path, message, null);
    }

}
