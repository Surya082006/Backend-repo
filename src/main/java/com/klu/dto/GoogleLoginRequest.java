package com.klu.dto;

import lombok.Data;

@Data
public class GoogleLoginRequest {
    private String credential;

    public GoogleLoginRequest() {}

    public GoogleLoginRequest(String credential) {
        this.credential = credential;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
