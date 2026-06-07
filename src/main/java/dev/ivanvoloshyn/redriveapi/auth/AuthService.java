package dev.ivanvoloshyn.redriveapi.auth;

import dev.ivanvoloshyn.redriveapi.auth.model.LoginRequest;
import dev.ivanvoloshyn.redriveapi.auth.model.LoginResponse;
import dev.ivanvoloshyn.redriveapi.exception.InvalidCredentialsException;
import dev.ivanvoloshyn.redriveapi.security.PasswordHasher;
import dev.ivanvoloshyn.redriveapi.user.UserRepository;
import dev.ivanvoloshyn.redriveapi.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String ERROR_INVALID_CREDENTIALS = "Invalid email or password";

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException(ERROR_INVALID_CREDENTIALS));
        if (!passwordHasher.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException(ERROR_INVALID_CREDENTIALS);
        }

        return new LoginResponse("Login successful");
    }


}
