package dev.ivanvoloshyn.redriveapi.user;

import dev.ivanvoloshyn.redriveapi.user.model.User;
import dev.ivanvoloshyn.redriveapi.user.model.UserResponse;

import java.util.Optional;

public interface UserRepository {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    UserResponse save(User user);
}
