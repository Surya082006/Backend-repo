package com.klu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.klu.model.User;
import com.klu.repository.UserRepository;
import com.klu.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // REGISTER
    public User register(User user) {

        repo.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exists");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repo.save(user);
    }

    // LOGIN → RETURN TOKEN
    public String login(String input, String password) {

        User user = repo.findByEmailOrUsername(input, input)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}