package com.pookietalk.services;

import com.pookietalk.dto.AuthRequestDTO;
import com.pookietalk.dto.AuthResponseDTO;
import com.pookietalk.models.Role;
import com.pookietalk.models.User;
import com.pookietalk.repositories.UserRepository;
import com.pookietalk.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordUtil passwordUtil;

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token, user.getId(), user.getUsername(), user.getEmail());
    }

    public AuthResponseDTO register(AuthRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordUtil.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user); // fix: get user ID after save

        UserDetails userDetails = userService.loadUserByUsername(savedUser.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token, savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}
