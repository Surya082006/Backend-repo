package com.klu.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String role;
    private String phone;
    
    // Educator fields
    private String qualification;
    private String specialization;
    
    // Student fields
    private String department;
    private String yearSemester;
}
