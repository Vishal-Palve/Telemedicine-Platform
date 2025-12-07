package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.ROLE_PATIENT);
    }

    @Test
    void registerUser_Success() {
        // Mock: username & email do not exist
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);

        // Mock encoding of password
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        // Mock save() to return a valid user
        User savedUser = new User(
                registerRequest.getUsername(),
                "encodedPassword",                // password (correct position)
                registerRequest.getEmail(),       // email
                registerRequest.getRole()
        );

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Call method
        User result = authService.registerUser(registerRequest);

        // Assertions
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());  // SUCCESS check
        assertEquals("test@example.com", result.getEmail());
        assertEquals(Role.ROLE_PATIENT, result.getRole());
    }

    @Test
    void registerUser_UsernameAlreadyExists() {
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(registerRequest);
        });

        assertEquals("Error: Username is already taken!", exception.getMessage());
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(registerRequest);
        });

        assertEquals("Error: Email is already in use!", exception.getMessage());
    }
}
