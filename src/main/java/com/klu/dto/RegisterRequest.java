package com.klu.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private UserDTO user;
    private String otp;

    public RegisterRequest() {
    }

    public RegisterRequest(UserDTO user, String otp) {
        this.user = user;
        this.otp = otp;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
