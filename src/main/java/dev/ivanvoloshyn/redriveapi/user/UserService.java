package dev.ivanvoloshyn.redriveapi.user;

import dev.ivanvoloshyn.redriveapi.user.exception.UserAlreadyExistsException;
import dev.ivanvoloshyn.redriveapi.user.model.RegisterUserRequest;
import dev.ivanvoloshyn.redriveapi.user.model.User;
import dev.ivanvoloshyn.redriveapi.user.model.UserResponse;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse registerUser(RegisterUserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new UserAlreadyExistsException(userRequest.email());
        }

        User user = UserMapper.toUser(userRequest, userRequest.password());
        return null;
    }

}
