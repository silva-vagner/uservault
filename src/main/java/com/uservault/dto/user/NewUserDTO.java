package com.uservault.dto.user;

import com.uservault.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public class NewUserDTO {
    @NotNull
    @NotEmpty(message = "O e-mail não pode estar vazio")
    @Email
    private String email;

    @NotNull
    @NotEmpty(message = "O nome não pode estar vazio")
    private String name;

    @NotNull
    @NotEmpty(message = "A senha não pode estar vazia")
    @Size(min = 6, max = 40, message = "A senha deve ter entre 6 e 40 caracteres")
    private String password;


    public NewUserDTO() {
    }

    public NewUserDTO(String name, String email, String password) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public NewUserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

