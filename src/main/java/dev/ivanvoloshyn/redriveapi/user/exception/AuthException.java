package dev.ivanvoloshyn.redriveapi.user.exception;

public abstract class AuthException extends RuntimeException {
    protected AuthException(String message) {
        super(message);
    }
}

