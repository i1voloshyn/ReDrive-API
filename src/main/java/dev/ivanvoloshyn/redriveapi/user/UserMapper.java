package dev.ivanvoloshyn.redriveapi.user;

import dev.ivanvoloshyn.redriveapi.user.model.RegisterUserRequest;
import dev.ivanvoloshyn.redriveapi.user.model.User;

public final class UserMapper {
    private UserMapper() {

    }

    public static User toUser(RegisterUserRequest registerUserRequest, String passwordHash) {
        return new User(
                null,
                registerUserRequest.email(),
                registerUserRequest.firstName(),
                registerUserRequest.lastName(),
                passwordHash
        );
    }
}
