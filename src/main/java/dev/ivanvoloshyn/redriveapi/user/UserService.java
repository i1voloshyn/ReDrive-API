package dev.ivanvoloshyn.redriveapi.user;

import dev.ivanvoloshyn.redriveapi.security.PasswordHasher;
import dev.ivanvoloshyn.redriveapi.user.exception.UserAlreadyExistsException;
import dev.ivanvoloshyn.redriveapi.user.model.RegisterUserRequest;
import dev.ivanvoloshyn.redriveapi.user.model.User;
import dev.ivanvoloshyn.redriveapi.user.model.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public UserResponse registerUser(RegisterUserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new UserAlreadyExistsException(userRequest.email());
        }
        User savedUser = userRepository.save(prepareUser(userRequest));
        return UserMapper.toResponse(savedUser);
    }

    private User prepareUser(RegisterUserRequest userRequest) {
        String hashedPassword = passwordHasher.hash(userRequest.password());
        return UserMapper.toUser(userRequest, hashedPassword);
    }

}
