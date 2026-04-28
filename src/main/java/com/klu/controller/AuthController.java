package com.klu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.klu.dto.AuthResponse;
import com.klu.dto.RegisterRequest;
import com.klu.dto.SendOtpRequest;
import com.klu.model.User;
import com.klu.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService service;

    // SEND OTP
    @PostMapping("/send-otp")
    public Map<String, String> sendOtp(@RequestBody SendOtpRequest request) {
        service.sendOtp(request.getEmail());
        return Map.of("message", "OTP sent successfully to " + request.getEmail());
    }

    // REGISTER
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {

        User savedUser = service.register(request.getUser(), request.getOtp());

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

    // GOOGLE LOGIN
    @PostMapping("/google-login")
    public Map<String, Object> googleLogin(@RequestBody com.klu.dto.GoogleLoginRequest request) {
        String clientId = "704973701323-cbjt0m8bt5oqnmfv1mbf2t3n3hphesh3.apps.googleusercontent.com";
        return service.googleLogin(request.getCredential(), clientId);
    }
}