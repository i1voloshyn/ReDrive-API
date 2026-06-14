package dev.ivanvoloshyn.redriveapi.exception.auth;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(Long id) {
        super("User with Id " + id + " not found");
    }
}
