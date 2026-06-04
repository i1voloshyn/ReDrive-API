package dev.ivanvoloshyn.redriveapi.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Pbkdf2PasswordHasher implements PasswordHasher {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int ITERATIONS = 100_000;
    private static final int KEY_LENGTH = 512;
    private static final int SALT_LENGTH = 32;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String hash(String password) {
        byte[] salt = generateSalt();
        byte[] hashedPassword;

        PasswordHashParameters parameters = new PasswordHashParameters(
                ALGORITHM,
                ITERATIONS,
                KEY_LENGTH,
                salt
        );

        hashedPassword = hashPassword(password, parameters);

        return encodeStoredPassword(hashedPassword, parameters);
    }

    @Override
    public boolean matches(String rawPassword, String storedPassword) {
        StoredPassword parsed = parseStoredPassword(storedPassword);

        PasswordHashParameters parameters = new PasswordHashParameters(parsed.algorithm(), parsed.iterations(),
                parsed.keyLength(), parsed.salt());

        byte[] hash = hashPassword(rawPassword, parameters);

        return MessageDigest.isEqual(hash, parsed.hash());
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private String encodeStoredPassword(byte[] hash, PasswordHashParameters parameters) {
        return parameters.algorithm() + ":" + parameters.iterations()
                + ":" + parameters.keyLength()
                + ":" + bytesToString(parameters.salt())
                + ":" + bytesToString(hash);
    }

    private String bytesToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private StoredPassword parseStoredPassword(String storedPassword) {
        String[] parts = storedPassword.split(":");

        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid hashed password format");
        }

        String algorithm = parts[0];
        int iterations = Integer.parseInt(parts[1]);
        int keyLength = Integer.parseInt(parts[2]);
        byte[] salt = stringToBytes(parts[3]);
        byte[] hash = stringToBytes(parts[4]);
        return new StoredPassword(algorithm, iterations, keyLength, salt, hash);
    }

    private byte[] hashPassword(String rawPassword, PasswordHashParameters ps) {
        PBEKeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), ps.salt(), ps.iterations(), ps.keyLength());

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ps.algorithm());
            return skf.generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } finally {
            spec.clearPassword();
        }
    }

    private byte[] stringToBytes(String string) {
        return Base64.getDecoder().decode(string);
    }

    private record StoredPassword(String algorithm, int iterations, int keyLength, byte[] salt, byte[] hash) {
    }

    private record PasswordHashParameters(String algorithm, int iterations, int keyLength, byte[] salt) {
    }

}
