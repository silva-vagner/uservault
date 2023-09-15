package com.uservault.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uservault.model.Role;
import com.uservault.model.User;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public class UserCreatedDTO {

    @JsonProperty("UUID")
    private UUID id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("roles")
    private List<String> roles;

    public UserCreatedDTO() {
    }

    public UserCreatedDTO(UUID id, String email, String name, List<String> roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.roles = roles;
    }

    public UserCreatedDTO(User user) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.roles = user.getRoles().stream().map(Role::getAuthority).toList();
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

