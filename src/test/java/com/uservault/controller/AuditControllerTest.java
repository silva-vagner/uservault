package com.uservault.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uservault.dto.user.AdminAuditDTO;
import com.uservault.dto.user.AdminStatisticsDetailsDTO;
import com.uservault.dto.user.AuditDetailDTO;
import com.uservault.dto.user.UserAuditDTO;
import com.uservault.dto.user.UserAuditDetailsDTO;
import com.uservault.model.Action;
import com.uservault.service.UserAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class AuditControllerTest {

    @InjectMocks
    private AuditController auditController;

    @Mock
    private UserAuditService userAuditService;

    @Mock
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(auditController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldGetUserAudit() throws Exception {
        UUID uuid = UUID.randomUUID();
        List<UserAuditDTO> userAuditDTOList = List.of(buildUserAuditDTO(uuid));

        when(userAuditService.getUserAudit("email@email", uuid)).thenReturn(userAuditDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/audit/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("email", "email@email")
                        .param("user_id", String.valueOf(uuid))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(userAuditService, times(1)).getUserAudit("email@email", uuid);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldGetAdminAudit() throws Exception {
        UUID uuid = UUID.randomUUID();
        AdminAuditDTO adminAuditDTO = buildAdminAuditDTO();

        when(userAuditService.getAdminAuditByEmailOrId("email@email", uuid)).thenReturn(adminAuditDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/audit/admin")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("email", "email@email")
                        .param("user_id", String.valueOf(uuid))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(userAuditService, times(1)).getAdminAuditByEmailOrId("email@email", uuid);
    }

    private UserAuditDTO buildUserAuditDTO(UUID uuid) {
        try {
            JsonNode previous = objectMapper.readTree("{\"name\": \"user\"}");
            JsonNode current = objectMapper.readTree("{\"name\": \"new user name\"}");

            AuditDetailDTO details = new AuditDetailDTO(previous, current, null);
            UserAuditDetailsDTO userPerformedAction = new UserAuditDetailsDTO(UUID.randomUUID(),"user", "user@email");
            UserAuditDetailsDTO userRelated = new UserAuditDetailsDTO(UUID.randomUUID(),"user", "user@email");

            return new UserAuditDTO(UUID.randomUUID(), Action.INSERTED, LocalDateTime.now(), details, userPerformedAction, userRelated);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AdminAuditDTO buildAdminAuditDTO() {
        UserAuditDetailsDTO userAuditDetailsDTO = new UserAuditDetailsDTO(UUID.randomUUID(), "name", "email@email");
        AdminStatisticsDetailsDTO adminStatisticsDetailsDTO = new AdminStatisticsDetailsDTO(Action.INSERTED, anyInt(), List.of(userAuditDetailsDTO));
        return new AdminAuditDTO(UUID.randomUUID(), "admin", anyInt(), List.of(adminStatisticsDetailsDTO));
    }
}

