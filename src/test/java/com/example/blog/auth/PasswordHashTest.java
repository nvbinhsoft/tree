package com.example.blog.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHashTest {

    @Test
    void printsKnownBcryptHashForPassword() {
        String bcryptHash = "$2y$10$z6AIUy4vVJDfpTkTnRaQ9.8BYt0vL8zqvRjgvCoPEN058KxnAUChW";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("BCrypt hash for 'password': " + bcryptHash);
        assertTrue(encoder.matches("password", bcryptHash), "Hash should match plaintext 'password'");
    }

    @Test
    void generatesAndPrintsHashForRawValue() {
        String raw = "password";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String generated = encoder.encode(raw);
        System.out.println("Generated bcrypt hash for '" + raw + "': " + generated);

        assertTrue(encoder.matches(raw, generated), "Generated hash should match the raw value");
    }
}
