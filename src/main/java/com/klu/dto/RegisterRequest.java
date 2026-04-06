package com.klu.dto;

import com.klu.model.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private User user;
    private String otp;
}
