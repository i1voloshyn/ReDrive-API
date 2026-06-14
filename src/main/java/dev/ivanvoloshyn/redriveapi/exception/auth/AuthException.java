package dev.ivanvoloshyn.redriveapi.exception.auth;

public abstract class AuthException extends RuntimeException {
    protected AuthException(String message) {
        super(message);
    }
}

