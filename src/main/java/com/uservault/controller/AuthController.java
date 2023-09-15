package com.uservault.controller;

import com.uservault.dto.auth.AuthenticationDTO;
import com.uservault.dto.auth.LoginResponseDTO;
import com.uservault.dto.auth.RegistrationDTO;
import com.uservault.dto.user.UserCreatedDTO;
import com.uservault.enums.RoleType;
import com.uservault.exception.UserRemovedException;
import com.uservault.model.User;
import com.uservault.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Recursos relacionados à autenticação.")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtDecoder jwtDecoder;

    @PostMapping("/register")
    @Operation(description = "Permite registro na aplicação com função USER.")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationDTO registrationDTO){
        User user = authService.registerUser(registrationDTO, List.of(RoleType.USER));
        if(user == null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email já está em uso!");
        }
        UserCreatedDTO userDTO = new UserCreatedDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    @Operation(description = "Autenticação de usuário. Retorna token JWT.")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationDTO authenticationDTO) {
        LoginResponseDTO userResponse = authService.loginUser(authenticationDTO.getEmail(), authenticationDTO.getPassword());
        if (userResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Dados inválidos!");
        }

        Jwt jwt = jwtDecoder.decode(userResponse.getJwt());

        if ((boolean) jwt.getClaim("isDeleted")) {
            throw new UserRemovedException();
        }

        return ResponseEntity.ok(userResponse);
    }
}
