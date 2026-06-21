package dev.ivanvoloshyn.redriveapi.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "First name is required")
        @Size(min = 2, message = "Size must be min 2 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, message = "Size must be min 2 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password is not valid")
        String password
) {
}

//@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
//        message = "Password must contain at least 1 digit, 1 lowercase, " +
//                "1 uppercase, 1 special character, and be at least 8 characters long") maybe later

