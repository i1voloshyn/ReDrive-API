package dev.ivanvoloshyn.redriveapi.exception.model.auth;

import dev.ivanvoloshyn.redriveapi.exception.model.ApiException;
import org.springframework.http.HttpStatus;

public abstract class AuthException extends ApiException {
    protected AuthException(String message, HttpStatus status) {
        super(message, status);
    }
}
