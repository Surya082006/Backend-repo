package com.klu.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String username;
    private String email;
    private String role;
}