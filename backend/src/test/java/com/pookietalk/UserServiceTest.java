package com.pookietalk;

import com.pookietalk.dto.UserDTO;
import com.pookietalk.exceptions.UserNotFoundException;
import com.pookietalk.models.Role;
import com.pookietalk.models.User;
import com.pookietalk.repositories.UserRepository;
import com.pookietalk.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByUsernameSuccess() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUserByUsername("testuser");

        assertNotNull(userDTO);
        assertEquals("testuser", userDTO.getUsername());
        assertEquals("test@example.com", userDTO.getEmail());
    }

    @Test
    void testGetUserByUsernameNotFound() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername("nonexistentuser"));
    }

    @Test
    void testGetUserByIdSuccess() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUserById(1L);

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        assertEquals("testuser", userDTO.getUsername());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setRole(Role.USER);

        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> userDTOs = userService.getAllUsers();

        assertEquals(2, userDTOs.size());
        assertEquals("user1", userDTOs.get(0).getUsername());
        assertEquals("user2", userDTOs.get(1).getUsername());
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedpassword");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedpassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))); // Fixed line
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("ghost"));
    }
}
