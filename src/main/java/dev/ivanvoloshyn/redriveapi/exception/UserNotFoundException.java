package dev.ivanvoloshyn.redriveapi.exception;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(Long id) {
        super("User with Id " + id + " not found");
    }
}
