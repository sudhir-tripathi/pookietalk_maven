package com.pookietalk.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

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
                .password("Test@123")
                .email("test@example.com")
                .role(Role.USER)
                .build();
    }

    @Test
    void whenAllFieldsAreValidThenValidationSucceeds() {
        var violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenUsernameIsBlankThenValidationFails() {
        user.setUsername("");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size()); // Size and NotBlank violations
    }

    @Test
    void whenEmailIsInvalidThenValidationFails() {
        user.setEmail("invalid-email");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void whenPasswordIsTooShortThenValidationFails() {
        user.setPassword("Weak@1");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void whenPasswordLacksRequiredCharactersThenValidationFails() {
        user.setPassword("weakpassword");
        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void getAuthoritiesShouldReturnCorrectRole() {
        var authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void accountStatusDefaultsShouldBeTrue() {
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void whenAccountStatusIsFalseThenMethodsShouldReturnFalse() {
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