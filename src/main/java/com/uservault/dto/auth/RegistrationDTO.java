package com.uservault.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public class RegistrationDTO {
    @NotNull
    @NotEmpty(message = "O e-mail não pode estar vazio")
    @Email
    private String email;

    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty(message = "A senha não pode estar vazia")
    @Size(min = 6, max = 40, message = "A senha deve ter entre 6 e 40 caracteres")
    private String password;

    public RegistrationDTO() {
    }

    public RegistrationDTO(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
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

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
