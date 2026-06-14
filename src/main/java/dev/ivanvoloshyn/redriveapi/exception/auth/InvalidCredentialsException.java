package dev.ivanvoloshyn.redriveapi.exception.auth;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
