package com.uservault.service;

import com.uservault.dto.user.UserCreatedDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

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
    public void shoudGetAllUsersDTO() {
        int page = 0;
        int size = 10;

        List<User> userList = List.of(buildNewUser());
        Page<User> userPage = new PageImpl<>(userList, PageRequest.of(page, size), userList.size());

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<UserCreatedDTO> resultPage = userService.getAllUsersDTO(page, size);

        assertEquals(userList.size(), resultPage.getContent().size());

        for (int i = 0; i < userList.size(); i++) {
            UserCreatedDTO userCreatedDTO = resultPage.getContent().get(i);
            User user = userList.get(i);
            assertEquals(user.getName(), userCreatedDTO.getName());
            assertEquals(user.getEmail(), userCreatedDTO.getEmail());
        }
    }

    User buildNewUser() {
        Set<Role> roles = Set.of(new Role(UUID.randomUUID(), "USER"));
        return new User(UUID.randomUUID(), "email@email", "nome", "123456", roles);
    }

}

