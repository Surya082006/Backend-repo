package com.klu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.klu.model.User;
import com.klu.repository.UserRepository;
import com.klu.security.JwtUtil;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    // SEND OTP
    public void sendOtp(String email) {
        repo.findByEmail(email).ifPresent(u -> {
            throw new RuntimeException("Email already exists");
        });

        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        emailService.sendEmail(email, "Your OTP for Registration", "Your OTP is: " + otp + "\nIt is valid for your current registration session.");
    }

    // REGISTER
    public User register(User user, String otp) {

        repo.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exists");
        });

        String storedOtp = otpStorage.get(user.getEmail());
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new RuntimeException("Invalid or missing OTP");
        }

        otpStorage.remove(user.getEmail());

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