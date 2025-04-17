package com.pookietalk.controllers;

import com.pookietalk.dto.AuthRequestDTO;
import com.pookietalk.dto.AuthResponseDTO;
import com.pookietalk.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void testSuccessfulRegistration() throws Exception {
        // Given
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("testuser");
        request.setPassword("Test@123");
        request.setConfirmPassword("Test@123");
        request.setEmail("test@example.com");

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken("test-token");
        response.setUsername("testuser");

        when(authService.register(any())).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"Test@123\",\"confirmPassword\":\"Test@123\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testInvalidRegistration() throws Exception {
        // When/Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"\",\"password\":\"\",\"confirmPassword\":\"\",\"email\":\"invalid-email\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        // Given
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken("test-token");
        response.setUsername("testuser");

        when(authService.authenticate(any())).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"Test@123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testInvalidLogin() throws Exception {
        // When/Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}