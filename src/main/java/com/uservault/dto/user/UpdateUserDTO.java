package com.uservault.dto.user;

import com.uservault.model.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public class UpdateUserDTO {

    @Nullable
    @Email
    private String email;

    @NotNull
    private String name;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UpdateUserDTO(User user) {
        this.name = user.getName();
    }


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

