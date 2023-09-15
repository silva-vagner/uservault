package com.uservault.dto.user;

import com.uservault.model.Action;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserAuditDTO {
    private UUID auditId;
    private Action action;
    private LocalDateTime actionDate;
    private AuditDetailDTO details;
    private UserAuditDetailsDTO userPerformedAction;
    private UserAuditDetailsDTO userRelated;

    public UserAuditDTO(UUID auditId, Action action, LocalDateTime actionDate, AuditDetailDTO details,
                        UserAuditDetailsDTO userPerformedAction, UserAuditDetailsDTO userRelated) {
        this.auditId = auditId;
        this.action = action;
        this.actionDate = actionDate;
        this.details = details;
        this.userRelated = userRelated;
        this.userPerformedAction = userPerformedAction;
    }

    public UserAuditDTO(){}

    public UUID getAuditId() {
        return auditId;
    }

    public void setAuditId(UUID auditId) {
        this.auditId = auditId;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public AuditDetailDTO getDetails() {
        return details;
    }

    public void setDetails(AuditDetailDTO details) {
        this.details = details;
    }

    public UserAuditDetailsDTO getUserRelated() {
        return userRelated;
    }

    public void setUserRelated(UserAuditDetailsDTO userRelated) {
        this.userRelated = userRelated;
    }

    public UserAuditDetailsDTO getUserPerformedAction() {
        return userPerformedAction;
    }

    public void setUserPerformedAction(UserAuditDetailsDTO userPerformedAction) {
        this.userPerformedAction = userPerformedAction;
    }
}
