package com.klu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.klu.model.*;
import com.klu.dto.UserDTO;
import com.klu.repository.UserRepository;
import com.klu.security.JwtUtil;
import com.klu.exception.UserAlreadyExistsException;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
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
            throw new UserAlreadyExistsException("A user with this email already exists!");
        });

        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        emailService.sendEmail(email, "Your OTP for Registration", "Your OTP is: " + otp + "\nIt is valid for your current registration session.");
    }

    // REGISTER
    public User register(UserDTO dto, String otp) {
        String requestedRole = dto.getRole() == null ? "" : dto.getRole().trim().toUpperCase();

        if (!"STUDENT".equals(requestedRole) && !"EDUCATOR".equals(requestedRole)) {
            throw new RuntimeException("Registration is only available for students and educators.");
        }

        repo.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("A user with this email already exists!");
        });

        if ("STUDENT".equals(requestedRole)) {
            String storedOtp = otpStorage.get(dto.getEmail());
            if (storedOtp == null || !storedOtp.equals(otp)) {
                throw new RuntimeException("Invalid or missing OTP");
            }
            otpStorage.remove(dto.getEmail());
        }

        User user;
        if ("EDUCATOR".equals(requestedRole)) {
            Educator e = new Educator();
            e.setQualification(dto.getQualification());
            e.setSpecialization(dto.getSpecialization());
            e.setApproved(false);
            user = e;
        } else {
            Student s = new Student();
            s.setDepartment(dto.getDepartment());
            s.setYearSemester(dto.getYearSemester());
            s.setApproved(true);
            user = s;
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(requestedRole);
        user.setPhone(dto.getPhone());

        return repo.save(user);
    }

    // LOGIN → RETURN TOKEN
    public String login(String input, String password) {

        User user = repo.findByEmailOrUsername(input, input)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isApproved()) {
            throw new RuntimeException("Your account is pending approval from the Super Admin.");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }

    public Map<String, Object> googleLogin(String credential, String clientId) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(credential);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                User user = repo.findByEmail(email).orElse(null);

                if (user != null) {
                    if (!user.isApproved()) {
                        throw new RuntimeException("Your account is pending approval from the Super Admin.");
                    }
                    String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "SUCCESS");
                    response.put("token", token);
                    return response;
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "USER_NOT_FOUND");
                    response.put("email", email);
                    response.put("name", name);
                    return response;
                }
            } else {
                throw new RuntimeException("Invalid Google ID token.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Google Authentication Failed: " + e.getMessage());
        }
    }
}

