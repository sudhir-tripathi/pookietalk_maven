package com.pookietalk.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utility class for password hashing and verification using BCrypt.
 */
@Component
public class PasswordUtil {

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a PasswordUtil with a configurable BCrypt strength (cost factor).
     *
     * @param strength the log rounds to use, between 4 and 31
     */
    public PasswordUtil(@Value("${security.bcrypt.strength:12}") int strength) {
        this.passwordEncoder = new BCryptPasswordEncoder(strength);
    }

    /**
     * Hashes a plain text password using BCrypt.
     *
     * @param rawPassword the raw password
     * @return the hashed password
     */
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verifies a raw password against its hashed form.
     *
     * @param rawPassword     the raw password
     * @param encodedPassword the hashed password
     * @return true if the raw password matches the hashed one
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
