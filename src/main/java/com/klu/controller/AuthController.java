package com.klu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.klu.dto.AuthResponse;
import com.klu.model.User;
import com.klu.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService service;

    // REGISTER
    @PostMapping("/register")
    public AuthResponse register(@RequestBody User user) {

        User savedUser = service.register(user);

        return new AuthResponse(
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    // LOGIN
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> data) {

        String token = service.login(
                data.get("email"),
                data.get("password")
        );

        return Map.of("token", token);
    }
}