package com.pookietalk.controllers;

import com.pookietalk.dto.AuthRequestDTO;
import com.pookietalk.dto.AuthResponseDTO;
import com.pookietalk.exceptions.ValidationException;
import com.pookietalk.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(getValidationErrors(result));
        }
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody AuthRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(getValidationErrors(result));
        }

        // Check if passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("confirmPassword", "Passwords do not match");
            throw new ValidationException(errors);
        }

        return ResponseEntity.ok(authService.register(request));
    }

    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }
}