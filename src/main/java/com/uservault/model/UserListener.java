package com.uservault.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uservault.config.AuditorAwareImpl;
import com.uservault.util.BeanUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.uservault.model.Action.INSERTED;
import static com.uservault.model.Action.UPDATED;
import static jakarta.transaction.Transactional.TxType.MANDATORY;

public class UserListener {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditorAwareImpl auditorAware;

    @PrePersist
    public void prePersist(User user) {
        perform(user, INSERTED);
    }

    @PreUpdate
    public void preUpdate(User target) {
        perform(target, UPDATED);
    }

    @Transactional(MANDATORY)
    private void perform(User user, Action action) {
        try (EntityManager entityManager = BeanUtil.getBean(EntityManager.class)) {
            Optional<String> userId = auditorAware.getCurrentAuditor();
            User loggedUser = (userId.isPresent()) ? entityManager.find(User.class, UUID.fromString(userId.get())) : null;

            ObjectNode auditContent = objectMapper.createObjectNode();
            UserAudit userAudit = null;

            switch (action) {
                case INSERTED -> {
                    auditContent = buildJsonContent(user);
                    userAudit = new UserAudit(
                            objectMapper.writeValueAsString(auditContent), loggedUser, LocalDateTime.now(),
                            null, null, action, user
                    );
                }
                case UPDATED -> {
                    if(user.isDeleted()){
                        action = Action.DELETED;
                    }else{
                        auditContent = buildJsonContent(user);
                    }
                    userAudit = new UserAudit(
                            objectMapper.writeValueAsString(auditContent), null, null,
                            loggedUser, LocalDateTime.now(), action, user
                    );
                }
            }
            if (userAudit != null) {
                entityManager.persist(userAudit);
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    private ObjectNode buildJsonContent(User user) {
        List<String> fieldsToSave = Arrays.asList("name", "email", "authorities");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode changes = objectMapper.createObjectNode();

        Class<User> userClass = User.class;
        Field[] fields = userClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(user);

                if (fieldsToSave.contains(field.getName())) {
                    if (value instanceof LinkedHashSet<?> listValue) {
                        changes.set(field.getName(), objectMapper.valueToTree(listValue));
                    } else {
                        changes.put(field.getName(), value.toString());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return changes;
    }

}
