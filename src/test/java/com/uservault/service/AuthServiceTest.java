package com.uservault.service;

import com.uservault.dto.auth.LoginResponseDTO;
import com.uservault.dto.auth.RegistrationDTO;
import com.uservault.enums.RoleType;
import com.uservault.model.Role;
import com.uservault.model.User;
import com.uservault.repository.RoleRepository;
import com.uservault.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldPersistUser() {
        User newUser = buildNewUser();
        RegistrationDTO registrationDTO = new RegistrationDTO("email@email", "username", "password");
        List<RoleType> roles = List.of(RoleType.USER);
        String passwordEncoded = "password-encoded";
        Set<Role> authorities = Collections.singleton(new Role(UUID.randomUUID(), "USER"));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(passwordEncoder.encode(anyString())).thenReturn(passwordEncoded);
        when(roleRepository.findAllByAuthorityIn(List.of(anyString()))).thenReturn(authorities);

        User registeredUser = authService.registerUser(registrationDTO, roles);

        assertEquals("email@email", registeredUser.getEmail());
        assertEquals("nome", registeredUser.getName());
        assertEquals("123456", registeredUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldLoginUser() {
        String email = "email@example.com";
        String password = "password";

        Authentication authMock = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authMock);
        when(tokenService.generateJwt(authMock)).thenReturn("generatedToken");

        LoginResponseDTO loginResponse = authService.loginUser(email, password);

        assertEquals("generatedToken", loginResponse.getJwt());
    }

    User buildNewUser(){
        Set<Role> roles = Set.of(new Role(UUID.randomUUID(), "USER"));
        return new User(UUID.randomUUID(), "email@email", "nome", "123456", roles);
    }
}

