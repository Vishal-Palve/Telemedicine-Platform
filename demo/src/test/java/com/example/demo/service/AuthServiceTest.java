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

/**
 * Unit tests for the AuthService.
 * We use Mockito to mock the dependencies (UserRepository, PasswordEncoder)
 * so we can test the service logic in isolation.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks // This injects the mocked dependencies into AuthService
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        // Create a common request object for tests
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.ROLE_PATIENT);
    }

    @Test
    void registerUser_Success() {
        // Arrange: Configure the mocks' behavior for a successful registration
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        // Mock the save operation to return a user object
        User savedUser = new User(registerRequest.getUsername(), registerRequest.getEmail(), "encodedPassword", registerRequest.getRole());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act: Call the method we are testing
        User result = authService.registerUser(registerRequest);

        // Assert: Verify the results
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void registerUser_UsernameAlreadyExists() {
        // Arrange: Configure the mock to indicate the username already exists
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);

        // Act & Assert: Verify that the correct exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(registerRequest);
        });

        assertEquals("Error: Username is already taken!", exception.getMessage());
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        // Arrange: Configure the mock to indicate the email already exists
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // Act & Assert: Verify that the correct exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(registerRequest);
        });

        assertEquals("Error: Email is already in use!", exception.getMessage());
    }
}