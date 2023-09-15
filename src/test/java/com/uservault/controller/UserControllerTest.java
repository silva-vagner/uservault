package com.uservault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uservault.dto.user.UpdateUserDTO;
import com.uservault.dto.user.UserCreatedDTO;
import com.uservault.enums.RoleType;
import com.uservault.model.Role;
import com.uservault.model.User;
import com.uservault.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(userController).build();
    }

    @Test
    public void shouldGetUserProfile() throws Exception {
        UserCreatedDTO userDTO = buildUserDTO();

        when(userService.getUserDTOByUuid(anyString())).thenReturn(userDTO);
        when(principal.getName()).thenReturn(UUID.randomUUID().toString());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/profile")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(userDTO)));

        verify(userService, times(1)).getUserDTOByUuid(anyString());
    }

    @Test
    public void shouldUpdateUserDetails() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("newName", "new@email");
        UserCreatedDTO userUpdated = buildUserDTO();
        userUpdated.setName(updateUserDTO.getName());

        when(principal.getName()).thenReturn(UUID.randomUUID().toString());
        when(userService.updateUser(any(UUID.class), any(UpdateUserDTO.class), any()))
                .thenReturn(userUpdated);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users")
                        .content(new ObjectMapper().writeValueAsString(updateUserDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .principal(principal)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(userUpdated)));

        verify(userService, times(1)).updateUser(any(UUID.class), any(UpdateUserDTO.class), any());
    }

    private UserCreatedDTO buildUserDTO() {
        Set<Role> roles = Collections.singleton(new Role(UUID.randomUUID(), RoleType.USER.name()));
        User user = new User(UUID.randomUUID(), "email@email", "username", "password", roles);

        UserCreatedDTO userDTO = new UserCreatedDTO();
        userDTO.setId(user.getUserId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());

        return userDTO;
    }
}

