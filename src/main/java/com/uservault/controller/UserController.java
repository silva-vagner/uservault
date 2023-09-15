package com.uservault.controller;

import com.uservault.dto.user.UpdateUserDTO;
import com.uservault.exception.EmailInUseException;
import com.uservault.repository.UserRepository;
import com.uservault.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@Tag(name = "User", description = "Recursos disponíveis para usuários comuns.")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/profile")
    @Operation(description = "Retorna detalhes do usuário logado")
    public ResponseEntity<?> getUserProfile(Principal principal) {
        String uuid = principal.getName();
        return ResponseEntity.ok(userService.getUserDTOByUuid(uuid));
    }

    @PatchMapping
    @Operation(description = "Atualiza detalhes do usuário logado.")
    public ResponseEntity<?> updateUserDetails(@RequestBody UpdateUserDTO updateUserDTO, Principal principal) {
        try{
            return ResponseEntity.ok(userService.updateUser(UUID.fromString(principal.getName()), updateUserDTO, null));
        }catch(EmailInUseException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já está em uso!");
        }
    }

}


