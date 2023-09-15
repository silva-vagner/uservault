package com.uservault.dto.user;

import com.uservault.model.User;

import java.util.UUID;

public class UserAuditDetailsDTO {
    private UUID userId;
    private String name;
    private String email;

    public UserAuditDetailsDTO(UUID userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public UserAuditDetailsDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public UserAuditDetailsDTO(){};

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
