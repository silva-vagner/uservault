package com.uservault.dto.user;

import com.uservault.model.Action;

import java.util.List;

public class AdminStatisticsDetailsDTO {
    private Action actionType;
    private Integer totalActions;
    private List<UserAuditDetailsDTO> affectedUsers;

    public AdminStatisticsDetailsDTO(Action actionType, Integer totalActions, List<UserAuditDetailsDTO> affectedUsers) {
        this.actionType = actionType;
        this.totalActions = totalActions;
        this.affectedUsers = affectedUsers;
    }

    public AdminStatisticsDetailsDTO(){};

    public Action getActionType() {
        return actionType;
    }

    public void setActionType(Action actionType) {
        this.actionType = actionType;
    }

    public Integer getTotalActions() {
        return totalActions;
    }

    public void setTotalActions(Integer totalActions) {
        this.totalActions = totalActions;
    }

    public List<UserAuditDetailsDTO> getAffectedUsers() {
        return affectedUsers;
    }

    public void setAffectedUsers(List<UserAuditDetailsDTO> affectedUsers) {
        this.affectedUsers = affectedUsers;
    }
}
