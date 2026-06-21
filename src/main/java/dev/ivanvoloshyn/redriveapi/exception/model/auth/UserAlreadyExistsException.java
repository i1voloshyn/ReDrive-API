package dev.ivanvoloshyn.redriveapi.exception.model.auth;

import org.springframework.http.HttpStatus;


public class UserAlreadyExistsException extends AuthException {

    public UserAlreadyExistsException(String email) {
        super("User already exists with email: " + email, HttpStatus.CONFLICT);
    }

}
