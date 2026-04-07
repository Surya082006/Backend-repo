package com.klu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.klu.model.User;
import com.klu.dto.UserDTO;
import com.klu.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // 👤 Get profile
    @GetMapping("/user/profile")
    public User getProfile(Authentication auth) {
        return userService.getUser(auth.getName());
    }

    // ✏️ Update profile
    @PutMapping("/user/profile")
    public User updateProfile(@RequestBody UserDTO userDTO, Authentication auth) {
        return userService.updateUser(auth.getName(), userDTO);
    }
}