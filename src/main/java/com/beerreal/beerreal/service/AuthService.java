package com.beerreal.beerreal.service;

import com.beerreal.beerreal.dto.AuthResponse;
import com.beerreal.beerreal.dto.LoginRequest;
import com.beerreal.beerreal.dto.RegisterRequest;
import com.beerreal.beerreal.model.User;
import com.beerreal.beerreal.repository.UserRepository;
import com.beerreal.beerreal.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Check if username exists
        if (userRepository.existsByUsername(
                request.getUsername())) {
            throw new RuntimeException(
                    "Username is already in use");
        }

        // Check if email exists
        if (userRepository.existsByEmail(
                request.getEmail())) {
            throw new RuntimeException(
                    "Email is already in use");
        }

        // Create new user
        User user = new User();
        user.setUsername(
                request.getUsername());
        user.setEmail(
                request.getEmail());
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(
                user.getUsername());

        return new AuthResponse(token,
                user.getUsername(),
                user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        // Get user
        User user = userRepository.findByUsername(
                        request.getUsername())
                .orElseThrow(
                        () -> new RuntimeException(
                                "Username not found"));

        // Generate token
        String token = jwtUtil.generateToken(
                user.getUsername());

        return new AuthResponse(token,
                user.getUsername(),
                user.getEmail());
    }
}
