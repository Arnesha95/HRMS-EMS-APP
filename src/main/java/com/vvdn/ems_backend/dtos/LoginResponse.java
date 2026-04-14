package com.vvdn.ems_backend.dtos;

import com.vvdn.ems_backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String message;
    private String role;
    private String username;

    public LoginResponse(){

    }

    public LoginResponse(String accessToken, String refreshToken, String message, String role, String username) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
        this.role = role;
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
