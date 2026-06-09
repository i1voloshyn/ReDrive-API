package dev.ivanvoloshyn.redriveapi.auth;

import dev.ivanvoloshyn.redriveapi.auth.model.ChangePasswordRequest;
import dev.ivanvoloshyn.redriveapi.auth.model.LoginRequest;
import dev.ivanvoloshyn.redriveapi.auth.model.LoginResponse;
import dev.ivanvoloshyn.redriveapi.user.UserService;
import dev.ivanvoloshyn.redriveapi.user.model.RegisterUserRequest;
import dev.ivanvoloshyn.redriveapi.user.model.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }

}
