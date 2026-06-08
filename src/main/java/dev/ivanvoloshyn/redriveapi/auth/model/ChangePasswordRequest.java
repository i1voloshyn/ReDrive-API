package dev.ivanvoloshyn.redriveapi.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String oldPassword,
        @NotBlank
        @Size(min = 8)
        String newPassword
) {
}
