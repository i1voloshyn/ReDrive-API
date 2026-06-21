package dev.ivanvoloshyn.redriveapi.exception.model.auth;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(Long id) {
        super("User with Id " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
