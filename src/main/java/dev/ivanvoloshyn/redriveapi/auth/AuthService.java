package dev.ivanvoloshyn.redriveapi.auth;

import dev.ivanvoloshyn.redriveapi.security.PasswordHasher;
import dev.ivanvoloshyn.redriveapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;



}
