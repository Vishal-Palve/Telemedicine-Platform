package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
// This will be needed in the next step, you can add it now
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    // We will inject this later when we set up Spring Security
    // For now, let's imagine we have a password encoder.
    // @Autowired
    // PasswordEncoder encoder;

    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        // NOTE: We are storing the password in plain text for now.
        // This is VERY INSECURE and will be fixed in the security step.
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                // encoder.encode(registerRequest.getPassword()), // We will use this later
                registerRequest.getPassword(),
                registerRequest.getRole()
        );

        return userRepository.save(user);
    }
}
