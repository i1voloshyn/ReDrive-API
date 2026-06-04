package dev.ivanvoloshyn.redriveapi.user.model;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}
