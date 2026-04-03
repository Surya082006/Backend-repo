package com.klu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.User;
import com.klu.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    // 🔍 Get user by email
    public User getUser(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✏️ Update user profile
    public User updateUser(String email, User updatedUser) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(updatedUser.getUsername());
        user.setPhone(updatedUser.getPhone());
        user.setQualification(updatedUser.getQualification());
        user.setDepartment(updatedUser.getDepartment());

        return userRepo.save(user);
    }
}