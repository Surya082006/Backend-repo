package com.klu.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private UserDTO user;
    private String otp;
}
