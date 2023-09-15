package com.uservault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uservault.dto.auth.RegistrationDTO;
import com.uservault.dto.user.UpdateUserDTO;
import com.uservault.enums.RoleType;
import com.uservault.model.Role;
import com.uservault.model.User;
import com.uservault.service.AuthService;
import com.uservault.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(adminController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldGetUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get( "/admin/users")
                        .accept(APPLICATION_JSON_VALUE)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllUsersDTO(anyInt(), anyInt());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldGetUserByEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get( "/admin/users/details-by-email")
                        .accept(APPLICATION_JSON_VALUE)
                        .param("email", "email@email")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserDTOByEmail(anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldCreateUserWithValidRegistration() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("email@email", "nome", "password");
        List<RoleType> roles = List.of(RoleType.ADMIN);
        User user = buildNewUser();

        when(authService.registerUser(any(), any())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationDTO))
                        .param("roles", RoleType.ADMIN.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(authService, times(1)).registerUser(any(RegistrationDTO.class), eq(roles));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldUpdateUser() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("email@email", "nome");
        List<RoleType> roles = List.of(RoleType.ADMIN);

        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users")
                        .content(new ObjectMapper().writeValueAsString(updateUserDTO))
                        .param("roles", RoleType.ADMIN.toString())
                        .param("email", "email@email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(anyString(), any(UpdateUserDTO.class), eq(roles));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldRemoveUserByEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", "email@email")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(userService, times(1)).deleteUserByEmail(anyString());
    }

    User buildNewUser(){
        Set<Role> roles = Set.of(new Role(UUID.randomUUID(), "USER"));
        return new User(UUID.randomUUID(), "email@email", "nome", "123456", roles);
    }

}

