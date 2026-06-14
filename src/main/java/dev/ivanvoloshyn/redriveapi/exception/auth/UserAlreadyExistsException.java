package dev.ivanvoloshyn.redriveapi.exception.auth;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String email) {
        super("User already exists with email: " + email);
    }
}
