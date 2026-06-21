package dev.ivanvoloshyn.redriveapi.user;

import dev.ivanvoloshyn.redriveapi.security.PasswordHasher;
import dev.ivanvoloshyn.redriveapi.exception.model.auth.UserAlreadyExistsException;
import dev.ivanvoloshyn.redriveapi.user.model.RegisterUserRequest;
import dev.ivanvoloshyn.redriveapi.user.model.User;
import dev.ivanvoloshyn.redriveapi.user.model.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordHasher passwordHasher;

    @InjectMocks
    UserService userService;

    @Test()
    void registerUser_ShouldSaveAndReturn_newUser() {
        RegisterUserRequest requestUser = createRegisterUserRequest();
        String encodedPassword = "encodedPassword";

        when(userRepository.existsByEmail(requestUser.email())).thenReturn(false);
        when(passwordHasher.hash(requestUser.password())).thenReturn(encodedPassword);

        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User userToSave = i.getArgument(0);
            userToSave.setId(1L);
            return userToSave;
        });

        UserResponse result = userService.registerUser(requestUser);

        verify(userRepository).existsByEmail(requestUser.email());
        verify(passwordHasher).hash(requestUser.password());

        ArgumentCaptor<User> captor = forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals(requestUser.email(), savedUser.getEmail());
        assertEquals(requestUser.firstName(), savedUser.getFirstName());
        assertEquals(requestUser.lastName(), savedUser.getLastName());
        assertEquals(encodedPassword, savedUser.getPasswordHash());

        assertEquals(1L, result.id());
        assertEquals(requestUser.email(), result.email());
        assertEquals(requestUser.firstName(), result.firstName());
        assertEquals(requestUser.lastName(), result.lastName());

    }

    @Test
    void registerUser_ShouldThrowUserAlreadyExistsException_WhenEmailExists() {
        RegisterUserRequest requestUser = createRegisterUserRequest();

        when(userRepository.existsByEmail(requestUser.email())).thenReturn(true);

        UserAlreadyExistsException exc = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.registerUser(requestUser));

        verify(userRepository).existsByEmail(requestUser.email());
        assertEquals("User already exists with email: test_email@email.com", exc.getMessage());
        verify(passwordHasher, never()).hash(any());
        verify(userRepository, never()).save(any(User.class));
    }

    private RegisterUserRequest createRegisterUserRequest() {
        return new RegisterUserRequest(
                "User",
                "UserLastName",
                "test_email@email.com",
                "user_password"
        );
    }

}