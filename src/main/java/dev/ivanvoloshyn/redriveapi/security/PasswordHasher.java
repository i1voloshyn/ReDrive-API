package dev.ivanvoloshyn.redriveapi.security;

public interface PasswordHasher {

    /**
     * Generate hash from password securely
     */
   String hash(String password);

    boolean matches(String rawPassword, String hashedPassword);
}
