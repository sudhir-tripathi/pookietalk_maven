package com.pookietalk;

import com.pookietalk.dto.AuthRequestDTO;
import com.pookietalk.dto.AuthResponseDTO;
import com.pookietalk.models.Role;
import com.pookietalk.models.User;
import com.pookietalk.repositories.UserRepository;
import com.pookietalk.services.AuthService;
import com.pookietalk.services.JwtService;
import com.pookietalk.services.UserService;
import com.pookietalk.utils.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUserSuccess() {
        AuthRequestDTO request = new AuthRequestDTO("testuser", "password123", "test@email.com");
        User user = new User(1L, "testuser", "hashedpassword", "test@email.com", Role.USER);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("testuser", "password123"));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetails.getPassword()).thenReturn("hashedpassword");

        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mocked-jwt-token");

        AuthResponseDTO response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@email.com", response.getEmail());
    }

    @Test
    void testAuthenticateUserNotFound() {
        AuthRequestDTO request = new AuthRequestDTO("testuser", "password123", "test@email.com");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.authenticate(request));
    }

    @Test
    void testRegisterUserSuccess() {
        AuthRequestDTO request = new AuthRequestDTO("testuser", "password123", "test@email.com");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordUtil.encode("password123")).thenReturn("encoded-password");

        User savedUser = new User(1L, "testuser", "encoded-password", "test@email.com", Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetails.getPassword()).thenReturn("encoded-password");

        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mocked-jwt-token");

        AuthResponseDTO response = authService.register(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@email.com", response.getEmail());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        AuthRequestDTO request = new AuthRequestDTO("testuser", "password123", "test@email.com");

        User existingUser = new User(1L, "testuser", "hashedpassword", "test@email.com", Role.USER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }
}
