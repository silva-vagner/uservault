package com.uservault.dto.role;

import com.uservault.model.User;

import java.util.UUID;

public class UserRoleDTO {

    private UUID roleId;
    private String authority;

    public UserRoleDTO() {
    }

    public UserRoleDTO(UUID roleId, String authority) {
        this.roleId = roleId;
        this.authority = authority;
    }

    public UserRoleDTO(User user) {
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}

