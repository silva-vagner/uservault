package com.uservault.controller;

import com.uservault.dto.auth.RegistrationDTO;
import com.uservault.dto.user.UpdateUserDTO;
import com.uservault.dto.user.UserCreatedDTO;
import com.uservault.enums.RoleType;
import com.uservault.exception.EmailInUseException;
import com.uservault.exception.EmailNotFoundException;
import com.uservault.model.User;
import com.uservault.repository.UserRepository;
import com.uservault.service.AuthService;
import com.uservault.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Recursos exclusivos para administradores.")
@RequestMapping("/admin/users")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthService authService;

    @Operation(description = "Busca detalhes de todos os usuários registrados.")
    @GetMapping
    public ResponseEntity<Page<UserCreatedDTO>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Page<UserCreatedDTO> users = userService.getAllUsersDTO(page, size);
        return ResponseEntity.ok(users);
    }

    @Operation(description = "Busca detalhes de qualquer usuário através do email.")
    @GetMapping("/details-by-email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserDTOByEmail(email));
    }

    @PostMapping("/create-user")
    @Operation(description = "Criação de usuário com definição de funções.")
    public ResponseEntity<?> createUser(@RequestBody @Valid RegistrationDTO registrationDTO,
                                        @RequestParam List<RoleType> roles){
        User user = authService.registerUser(registrationDTO, roles);
        if(user == null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email já está em uso!");
        }
        return ResponseEntity.ok(new UserCreatedDTO(user));
    }

    @PatchMapping
    @Operation(description = "Atualiza detalhes de qualquer usuário.")
    public ResponseEntity<?> updateUser(@RequestParam String email,
                                                 @RequestParam (required = false) List<RoleType> roles,
                                                 @RequestBody UpdateUserDTO updateUserDTO) {
        try{
            return ResponseEntity.ok(userService.updateUser(email, updateUserDTO, roles));
        }catch(EmailNotFoundException | EmailInUseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(description = "Remove usuário.")
    public ResponseEntity<?> deleteUser(@RequestParam String email) {
        try{
            userService.deleteUserByEmail(email);
            return ResponseEntity.ok().body("Removido com sucesso!");
        }catch(EmailNotFoundException | EmailInUseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

}


