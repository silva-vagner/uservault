package com.uservault.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uservault.dto.user.AdminAuditDTO;
import com.uservault.dto.user.AdminStatisticsDetailsDTO;
import com.uservault.dto.user.AuditDetailDTO;
import com.uservault.dto.user.UserAuditDTO;
import com.uservault.dto.user.UserAuditDetailsDTO;
import com.uservault.enums.RoleType;
import com.uservault.exception.NoAuditException;
import com.uservault.exception.UserNotFoundException;
import com.uservault.model.Action;
import com.uservault.model.User;
import com.uservault.model.UserAudit;
import com.uservault.repository.RoleRepository;
import com.uservault.repository.UserAuditRepository;
import com.uservault.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserAuditService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserAuditRepository userAuditRepository;
    @Autowired
    private ObjectMapper objectMapper;


    public List<UserAuditDTO> getUserAudit(String email, UUID userId) {
        Boolean userExists = (email != null) ? userRepository.findByEmail(email).isPresent() : userRepository.findByUserId(userId).isPresent();
        if(!userExists){
            throw new UserNotFoundException();
        }
        List<User> userAdminList = userRepository.findByUserRole(RoleType.ADMIN.toString());
        List<UserAudit> auditList;
        auditList = (email != null) ? userAuditRepository.findByUserEmail(email) : userAuditRepository.findByUserUserId(userId);

        AtomicReference<String> previousAuditContent = new AtomicReference<>();

        return auditList.stream().map(audit -> {
            User userPerformedAction;
            LocalDateTime actionDate;
            AuditDetailDTO auditContentObject;

            if(audit.getAction().equals(Action.INSERTED)){
                Objects.requireNonNull(previousAuditContent).set(audit.getContentJson());
                userPerformedAction = userAdminList
                        .stream()
                        .peek(user -> {
                            if (audit.getCreatedBy() == null) {
                                audit.setCreatedBy(user);
                            }
                        })
                        .filter(user -> user.getUserId().equals(audit.getCreatedBy().getUserId()))
                        .findFirst()
                        .orElseThrow(UserNotFoundException::new);
                actionDate = audit.getCreatedDate();
            }else{
                userPerformedAction = userRepository.findByUserId(audit.getLastUpdatedBy().getUserId()).orElseThrow(UserNotFoundException::new);
                actionDate = audit.getLastUpdatedDate();
            }
            User user = userRepository.findByUserId(audit.getUser().getUserId()).orElseThrow(UserNotFoundException::new);

            UserAuditDetailsDTO userPerformedActionDTO = new UserAuditDetailsDTO(userPerformedAction.getUserId(),
                    userPerformedAction.getName(), userPerformedAction.getEmail());
            UserAuditDetailsDTO userRelated = new UserAuditDetailsDTO(user.getUserId(), user.getName(), user.getEmail());

            auditContentObject = buildAuditContentObject(audit, previousAuditContent.get());
            previousAuditContent.set(audit.getContentJson());

            return new UserAuditDTO(audit.getUserAuditId(), audit.getAction(), actionDate, auditContentObject,
                    userPerformedActionDTO, userRelated);
        }).toList();
    }

    private AuditDetailDTO buildAuditContentObject(UserAudit audit, String previousContentObject) {
        if(audit.getAction().equals(Action.INSERTED)){
            return new AuditDetailDTO(null, null, "Usuário criado");
        }
        if(audit.getAction().equals(Action.UPDATED)){
            try {
                JsonNode previous = objectMapper.readTree(previousContentObject);
                JsonNode current = objectMapper.readTree(audit.getContentJson());
                return new AuditDetailDTO(previous, current, null);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new AuditDetailDTO(null, null, "Usuário removido");
    }

    public AdminAuditDTO getAdminAuditByEmailOrId(String email, UUID userId) {
        List<UserAudit> auditList;
        auditList = (email != null) ? userAuditRepository.findAllRelatedAuditsByEmail(email)
                : userAuditRepository.findAllRelatedAuditsByUserId(userId);

        if (auditList.isEmpty()) {
            throw new NoAuditException();
        }

        UserAudit firstAudit = auditList.get(0);
        UUID adminId = firstAudit.getCreatedBy().getUserId();
        String adminName = firstAudit.getCreatedBy().getName();
        int auditCount = auditList.size();

        List<User> userInsertedList = new ArrayList<>();
        List<User> userUpdatedList = new ArrayList<>();
        List<User> userRemovedList = new ArrayList<>();

        auditList.forEach(audit -> {
            switch (audit.getAction()) {
                case INSERTED -> userInsertedList.add(audit.getUser());
                case UPDATED -> userUpdatedList.add(audit.getUser());
                case DELETED -> userRemovedList.add(audit.getUser());
            }
        });

        List<UserAuditDetailsDTO> userInsertedDTOList = userInsertedList.stream().map(UserAuditDetailsDTO::new).toList();
        List<UserAuditDetailsDTO> userUpdatedDTOList = userUpdatedList.stream().map(UserAuditDetailsDTO::new).toList();
        List<UserAuditDetailsDTO> userRemovedDTOList = userRemovedList.stream().map(UserAuditDetailsDTO::new).toList();

        List<AdminStatisticsDetailsDTO> adminStatisticsDetailsDTO = List.of(
                new AdminStatisticsDetailsDTO(Action.INSERTED, userInsertedList.size(), userInsertedDTOList),
                new AdminStatisticsDetailsDTO(Action.UPDATED, userUpdatedList.size(), userUpdatedDTOList),
                new AdminStatisticsDetailsDTO(Action.DELETED, userRemovedList.size(), userRemovedDTOList)
        );

        return new AdminAuditDTO(adminId, adminName, auditCount, adminStatisticsDetailsDTO);
    }
}

