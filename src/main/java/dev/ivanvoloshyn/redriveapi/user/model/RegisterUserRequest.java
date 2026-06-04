package dev.ivanvoloshyn.redriveapi.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank
        @Size(min = 2)
        String firstName,

        @NotBlank
        @Size(min = 2)
        String lastName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8)
        String password
) {
}

//@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
//        message = "Password must contain at least 1 digit, 1 lowercase, " +
//                "1 uppercase, 1 special character, and be at least 8 characters long") maybe later

