package com.uservault.dto.auth;

public class LoginResponseDTO {
    private String jwt;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
