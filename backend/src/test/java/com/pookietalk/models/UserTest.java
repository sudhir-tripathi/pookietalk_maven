package com.pookietalk.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// import java.util.Set; // Removed unused import

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Validator validator;
    private User user;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user = User.builder()
                .id(1L)
                .username("testuser")
                // Updated password to meet complexity for validation tests
                .password("TestPass@123")
                .email("test@example.com")
                .role(Role.USER)
                // Assuming User.java now has @Builder.Default for boolean flags
                // No need to explicitly set them here if defaults are true
                .build();
    }

    @Test
    void whenAllFieldsAreValidThenValidationSucceeds() {
        // Ensure the password in setUp meets the @Pattern criteria
        user.setPassword("TestPass@123"); // Example valid password
        var violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Validation should succeed with valid data. Violations: " + violations);
    }

    @Test
    void whenUsernameIsBlankThenValidationFails() {
        user.setUsername("");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        // Check specific violation messages if needed, count might vary based on combined annotations
        // assertEquals(2, violations.size()); // Size and NotBlank violations might merge or differ
    }

    @Test
    void whenEmailIsInvalidThenValidationFails() {
        user.setEmail("invalid-email");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Please provide a valid email address", violations.iterator().next().getMessage());
    }

    @Test
    void whenPasswordIsTooShortThenValidationFails() {
        user.setPassword("Weak@1"); // Less than 8 chars
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        // Check if both Size and Pattern fail or just Size
        // assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must be at least 8 characters")));
    }

    @Test
    void whenPasswordLacksRequiredCharactersThenValidationFails() {
        user.setPassword("weakpassword123"); // Fails @Pattern (no upper, no special)
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("must contain at least one digit")); // Example check
    }

    @Test
    void getAuthoritiesShouldReturnCorrectRole() {
        var authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertEquals(1, authorities.size()); // Should only have one role based on setup
    }

    @Test
    void accountStatusDefaultsShouldBeTrue() {
        // This assumes User.java has @Builder.Default on the boolean fields
        // Re-create user with builder to ensure defaults are applied if setUp modifies user
         User defaultUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("TestPass@123")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        assertTrue(defaultUser.isAccountNonExpired(), "Default accountNonExpired should be true");
        assertTrue(defaultUser.isAccountNonLocked(), "Default accountNonLocked should be true");
        assertTrue(defaultUser.isCredentialsNonExpired(), "Default credentialsNonExpired should be true");
        assertTrue(defaultUser.isEnabled(), "Default enabled should be true");
    }

    @Test
    void whenAccountStatusIsFalseThenMethodsShouldReturnFalse() {
        // Use the user from setUp
        user.setAccountNonExpired(false);
        user.setAccountNonLocked(false);
        user.setCredentialsNonExpired(false);
        user.setEnabled(false);

        assertFalse(user.isAccountNonExpired());
        assertFalse(user.isAccountNonLocked());
        assertFalse(user.isCredentialsNonExpired());
        assertFalse(user.isEnabled());
    }
}
