package dev.ivanvoloshyn.redriveapi.auth;

import dev.ivanvoloshyn.redriveapi.auth.model.LoginRequest;
import dev.ivanvoloshyn.redriveapi.auth.model.LoginResponse;
import dev.ivanvoloshyn.redriveapi.exception.InvalidCredentialsException;
import dev.ivanvoloshyn.redriveapi.security.PasswordHasher;
import dev.ivanvoloshyn.redriveapi.user.UserRepository;
import dev.ivanvoloshyn.redriveapi.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordHasher passwordHasher;
    @InjectMocks
    private AuthService authService;

    @Test
    void login_ShouldReturnSuccessResponse_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("login@email.com", "password");
        User user = new User(1L,
                "login@email.com",
                "Joe",
                "Toronto",
                "encodedPassword");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordHasher.matches(request.password(), user.getPasswordHash())).thenReturn(true);

        LoginResponse response = authService.login(request);

        verify(userRepository).findByEmail(request.email());
        verify(passwordHasher).matches(request.password(), user.getPasswordHash());

        assertEquals("Login successful", response.message());
    }

    @Test
    void login_shouldThrow_InvalidCredentialsException_whenUserDoesNotExist() {
        LoginRequest request = new LoginRequest("wrong@email.com", "password");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        InvalidCredentialsException e = assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(userRepository).findByEmail(request.email());
        verifyNoInteractions(passwordHasher);

        assertEquals("Invalid email or password", e.getMessage());
    }

    @Test
    void login_shouldThrow_InvalidCredentialsException_whenPasswordIsInvalid() {
        LoginRequest request = new LoginRequest("login@email.com", "wrong_password");
        User user = new User(1L,
                "login@email.com",
                "Joe",
                "Toronto",
                "encodedPassword");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordHasher.matches(request.password(), user.getPasswordHash())).thenReturn(false);

        InvalidCredentialsException e = assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(userRepository).findByEmail(request.email());
        verify(passwordHasher).matches(request.password(), user.getPasswordHash());

        assertEquals("Invalid email or password", e.getMessage());
    }

}