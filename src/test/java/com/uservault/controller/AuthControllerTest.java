package com.uservault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uservault.dto.auth.AuthenticationDTO;
import com.uservault.dto.auth.LoginResponseDTO;
import com.uservault.dto.auth.RegistrationDTO;
import com.uservault.dto.user.UserCreatedDTO;
import com.uservault.enums.RoleType;
import com.uservault.model.Role;
import com.uservault.model.User;
import com.uservault.service.AuthService;
import com.uservault.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtDecoder jwtDecoder;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(authController).build();
    }

    @Test
    public void shouldLoginUser() throws Exception {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("email@email", "name");
        String jwtString = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImV4cCI6MTY5MzEwMTMzOCwiaWF0IjoxNjE4MzA0NTM4fQ.QGObnJOMLzPFGbg1XChcw7S8L2y1o-fj6UasKvFjwzKMS7ABct5lX6BRsXKBfg8xZK7wY0IjGXFs9Xyp_r9yoQ;";
        Jwt jwt = Jwt.withTokenValue(jwtString)
                .header("alg", "HS256")
                .claim("sub", "email@email")
                .claim("isDeleted", false)
                .build();
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtString);

        when(authService.loginUser(authenticationDTO.getEmail(), authenticationDTO.getPassword()))
                .thenReturn(loginResponseDTO);
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authenticationDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(loginResponseDTO)));

        verify(authService, times(1)).loginUser(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void shouldRegisterNewUser() throws Exception {
        UUID uuid = UUID.randomUUID();
        Set<Role> roles = Collections.singleton(new Role(uuid, RoleType.USER.name()));
        RegistrationDTO registrationDTO = new RegistrationDTO("email@email", "name", "password");
        UserCreatedDTO userCreatedDTO = buildUserDTO(uuid);

        when(authService.registerUser(Mockito.any(RegistrationDTO.class), Mockito.anyList())).thenReturn(buildNewUser(uuid, roles));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(userCreatedDTO)));

        verify(authService, times(1)).registerUser(Mockito.any(RegistrationDTO.class), Mockito.anyList());
    }



    private UserCreatedDTO buildUserDTO(UUID uuid) {
        Set<Role> roles = Collections.singleton(new Role(uuid, RoleType.USER.name()));
        User user = new User(uuid, "email@email", "name", "password", roles);
        return new UserCreatedDTO(user);
    }

    private User buildNewUser(UUID uuid, Set<Role> roles) {
        return new User(uuid, "email@email", "name", "password", roles);
    }
}

