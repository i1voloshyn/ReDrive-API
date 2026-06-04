package dev.ivanvoloshyn.redriveapi.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Pbkdf2PasswordHasherTest {
    @Test
    void matches_ShouldReturnTrue_whenRawPasswordMatchesStoredHash() {
        PasswordHasher passwordHasher = new Pbkdf2PasswordHasher();
        String rawPassword = "Test123!@";

        String storedHash = passwordHasher.hash(rawPassword);

        boolean matches = passwordHasher.matches(rawPassword, storedHash);

        assertTrue(matches);

    }

    @Test
    void matches_ShouldReturnFalse_whenRawPasswordDoesNotMatchStoredHash() {
        PasswordHasher passwordHasher = new Pbkdf2PasswordHasher();
        String rawPassword = "Test123!@";

        String storedHash = passwordHasher.hash(rawPassword);

        boolean matches = passwordHasher.matches("WrongPassword", storedHash);

        assertFalse(matches);
    }

    @Test
    void hash_ShouldCreateDifferentHash_ForSamePassword(){
        PasswordHasher passwordHasher = new Pbkdf2PasswordHasher();
        String rawPassword = "Test123!@";

        String firstHash = passwordHasher.hash(rawPassword);
        String secondHash = passwordHasher.hash(rawPassword);

        assertNotEquals(firstHash, secondHash);
    }

}