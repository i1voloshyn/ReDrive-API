package dev.ivanvoloshyn.redriveapi.auth;

import dev.ivanvoloshyn.redriveapi.auth.model.ChangePasswordRequest;
import dev.ivanvoloshyn.redriveapi.auth.model.LoginRequest;
import dev.ivanvoloshyn.redriveapi.auth.model.LoginResponse;
import dev.ivanvoloshyn.redriveapi.exception.model.auth.InvalidCredentialsException;
import dev.ivanvoloshyn.redriveapi.security.PasswordHasher;
import dev.ivanvoloshyn.redriveapi.user.UserRepository;
import dev.ivanvoloshyn.redriveapi.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordHasher passwordHasher;
    @InjectMocks
    private AuthService authService;

    private static final String ERROR_INVALID_CREDENTIALS = "Invalid credentials";

    @Test
    void login_shouldReturnSuccessResponse_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("login@email.com", "password");
        String hashedPassword = "hashedPassword";
        User user = new User(1L,
                "login@email.com",
                "Joe",
                "Toronto",
                hashedPassword);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordHasher.matches(request.password(), user.getPasswordHash())).thenReturn(true);

        LoginResponse response = authService.login(request);

        verify(userRepository).findByEmail(request.email());
        verify(passwordHasher).matches(request.password(), user.getPasswordHash());

        assertEquals("Login successful", response.message());
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenUserNotFound() {
        LoginRequest request = new LoginRequest("wrong@email.com", "password");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        InvalidCredentialsException e = assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(userRepository).findByEmail(request.email());
        verifyNoInteractions(passwordHasher);

        assertEquals(ERROR_INVALID_CREDENTIALS, e.getMessage());
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenPasswordIsInvalid() {
        LoginRequest request = new LoginRequest("login@email.com", "wrong_password");
        String hashedPassword = "hashedPassword";
        User user = new User(1L,
                "login@email.com",
                "Joe",
                "Toronto",
                hashedPassword);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordHasher.matches(request.password(), user.getPasswordHash())).thenReturn(false);

        InvalidCredentialsException e = assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(userRepository).findByEmail(request.email());
        verify(passwordHasher).matches(request.password(), user.getPasswordHash());

        assertEquals(ERROR_INVALID_CREDENTIALS, e.getMessage());
    }

    @Test
    void changePassword_shouldThrowInvalidCredentialsException_whenUserNotFound() {
        ChangePasswordRequest request = new ChangePasswordRequest("wrong@email.com", "old_password", "new_password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        InvalidCredentialsException e = assertThrows(InvalidCredentialsException.class, () -> authService.changePassword(request));

        verify(userRepository).findByEmail(request.email());
        verifyNoInteractions(passwordHasher);

        assertEquals(ERROR_INVALID_CREDENTIALS, e.getMessage());
    }

    @Test
    void changePassword_shouldThrowInvalidCredentialsException_whenOldPasswordIsInvalid() {
        ChangePasswordRequest request = new ChangePasswordRequest("login@email.com", "wrong_password", "new_password");
        String hashedPassword = "hashedPassword";
        User user = new User(1L,
                "login@email.com",
                "Joe",
                "Toronto",
                hashedPassword);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordHasher.matches(request.oldPassword(), hashedPassword)).thenReturn(false);

        InvalidCredentialsException e = assertThrows(InvalidCredentialsException.class, () -> authService.changePassword(request));

        verify(userRepository).findByEmail(request.email());
        verify(passwordHasher).matches(request.oldPassword(), hashedPassword);
        verify(passwordHasher, never()).hash(anyString());

        assertEquals(ERROR_INVALID_CREDENTIALS, e.getMessage());
    }

    @Test
    void changePassword_shouldUpdatePasswordHash_whenCredentialsAreValid() {
        ChangePasswordRequest request = new ChangePasswordRequest("login@email.com", "old_password", "new_password");
        String newHashedPassword = "newHashPassword";
        String oldHashedPassword = "oldHashPassword";
        User user = new User(1L,
                "login@email.com",
                "Joe",
                "Toronto",
                oldHashedPassword);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordHasher.matches(request.oldPassword(), oldHashedPassword)).thenReturn(true);
        when(passwordHasher.hash(request.newPassword())).thenReturn(newHashedPassword);

        authService.changePassword(request);

        verify(userRepository).findByEmail(request.email());
        verify(passwordHasher).matches(request.oldPassword(), oldHashedPassword);
        verify(passwordHasher).hash(request.newPassword());

        assertEquals(newHashedPassword, user.getPasswordHash());
    }



}