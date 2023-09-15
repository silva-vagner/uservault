package com.uservault.dto.role;

import com.uservault.model.Role;

public class RoleDTO {

    private String roleName;

    public RoleDTO() {
    }

    public RoleDTO(String roleName) {
        this.roleName = roleName;
    }

    public RoleDTO(Role role) {
        this.roleName = role.getAuthority();
    }

    public String getAuthority() {
        return roleName;
    }

    public void setAuthority(String authority) {
        this.roleName = roleName;
    }
}

