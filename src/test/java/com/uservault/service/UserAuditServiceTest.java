package com.uservault.service;

import com.uservault.dto.user.AdminAuditDTO;
import com.uservault.model.Action;
import com.uservault.model.Role;
import com.uservault.model.User;
import com.uservault.model.UserAudit;
import com.uservault.repository.RoleRepository;
import com.uservault.repository.UserAuditRepository;
import com.uservault.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserAuditServiceTest {

    @InjectMocks
    private UserAuditService userAuditService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserAuditRepository userAuditRepository;

    @Mock
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldGetAdminAuditByEmail() {
        String email = "user@user";
        List<UserAudit> userAuditList = new ArrayList<>();
        UserAudit userAudit1 = new UserAudit(UUID.randomUUID(), "content", buildNewUser(), LocalDateTime.now(),
                buildNewUser(), LocalDateTime.now(), Action.INSERTED, buildNewUser());
        userAuditList.add(userAudit1);

        when(userAuditRepository.findAllRelatedAuditsByEmail(email)).thenReturn(userAuditList);
        AdminAuditDTO result = userAuditService.getAdminAuditByEmailOrId(email, null);

        assertNotNull(result);
    }

    @Test
    public void shouldGetAdminAuditByUserId() {
        UUID userId = UUID.randomUUID();
        List<UserAudit> userAuditList = new ArrayList<>();
        UserAudit userAudit1 = new UserAudit(UUID.randomUUID(), "content", buildNewUser(), LocalDateTime.now(),
                buildNewUser(), LocalDateTime.now(), Action.INSERTED, buildNewUser());
        userAuditList.add(userAudit1);

        when(userAuditRepository.findAllRelatedAuditsByUserId(userId)).thenReturn(userAuditList);
        AdminAuditDTO result = userAuditService.getAdminAuditByEmailOrId(null, userId);

        assertNotNull(result);
    }

    User buildNewUser() {
        Set<Role> roles = Set.of(new Role(UUID.randomUUID(), "USER"));
        return new User(UUID.randomUUID(), "email@email", "nome", "123456", roles);
    }

}

