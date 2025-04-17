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
import org.mockito.ArgumentCaptor; // Import ArgumentCaptor
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("testuser")
                .password("password123")
                .email("test@email.com") // Assuming email is part of AuthRequestDTO
                .build();

        User user = new User(1L, "testuser", "hashedpassword", "test@email.com", Role.USER);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Authentication successfulAuth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(successfulAuth);

        when(jwtService.generateToken(user)).thenReturn("mocked-jwt-token");

        AuthResponseDTO response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getEmail(), response.getEmail());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(user);
    }

    @Test
    void testAuthenticateUserNotFound() {
        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("nonexistentuser")
                .password("password123")
                .email("test@email.com")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> authService.authenticate(request));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any());
    }


    @Test
    void testRegisterUserSuccess() {
        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("newuser")
                .password("password123")
                .email("new@email.com")
                .build();

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordUtil.encode("password123")).thenReturn("encoded-password");

        // --- CHANGE: Use ArgumentCaptor to verify the User object being saved ---
        // This represents the expected state *before* saving (no ID, encoded password)
        User expectedUserBeforeSave = User.builder()
                .username(request.getUsername())
                .password("encoded-password") // Use the mocked encoded password
                .email(request.getEmail())
                .role(Role.USER) // Assuming default role is USER
                // Include defaults from User class if they should be set before save
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        // Define what the saved user should look like (mocked return from save)
        User savedUser = User.builder()
                .id(1L) // Example ID assigned by DB/JPA
                .username(request.getUsername())
                .password("encoded-password")
                .email(request.getEmail())
                .role(Role.USER)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
        // Capture the argument passed to save
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(savedUser);
        // --- END CHANGE ---

        when(jwtService.generateToken(savedUser)).thenReturn("mocked-jwt-token");

        // --- Act ---
        AuthResponseDTO response = authService.register(request);

        // --- Assert Response ---
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals(savedUser.getId(), response.getId());
        assertEquals(savedUser.getUsername(), response.getUsername());
        assertEquals(savedUser.getEmail(), response.getEmail());

        // --- Assert Interactions and Captured Argument ---
        verify(userRepository).save(any(User.class)); // Verify save was called
        User capturedUser = userCaptor.getValue(); // Get the captured user object

        // Assert that the captured user matches the expected state before saving
        // (ID will be null or 0 before save, password should be encoded)
        assertNull(capturedUser.getId()); // ID should be null before JPA assigns it
        assertEquals(expectedUserBeforeSave.getUsername(), capturedUser.getUsername());
        assertEquals(expectedUserBeforeSave.getPassword(), capturedUser.getPassword());
        assertEquals(expectedUserBeforeSave.getEmail(), capturedUser.getEmail());
        assertEquals(expectedUserBeforeSave.getRole(), capturedUser.getRole());
        // Optionally assert default boolean flags if important
        assertTrue(capturedUser.isEnabled());
        assertTrue(capturedUser.isAccountNonExpired());
        assertTrue(capturedUser.isAccountNonLocked());
        assertTrue(capturedUser.isCredentialsNonExpired());


        verify(jwtService).generateToken(savedUser); // Verify token generation with the *saved* user
    }

    @Test
    void testRegisterUserAlreadyExists() {
        AuthRequestDTO request = AuthRequestDTO.builder()
                .username("existinguser")
                .password("password123")
                .email("existing@email.com")
                .build();

        User existingUser = new User(1L, "existinguser", "hashedpassword", "existing@email.com", Role.USER);
        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));

        verify(userRepository).findByUsername("existinguser");
        verify(userRepository, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(any());
    }
}
