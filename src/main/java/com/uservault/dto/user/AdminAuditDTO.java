package com.uservault.dto.user;

import java.util.List;
import java.util.UUID;

public class AdminAuditDTO {
    private UUID adminId;
    private String adminName;
    private Integer totalActions;
    private List<AdminStatisticsDetailsDTO> adminStatisticsDetailsDTO;

    public AdminAuditDTO(UUID adminId, String adminName, Integer totalActions, List<AdminStatisticsDetailsDTO> adminStatisticsDetailsDTO) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.totalActions = totalActions;
        this.adminStatisticsDetailsDTO = adminStatisticsDetailsDTO;
    }

    public AdminAuditDTO() {
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Integer getTotalActions() {
        return totalActions;
    }

    public void setTotalActions(Integer totalActions) {
        this.totalActions = totalActions;
    }

    public List<AdminStatisticsDetailsDTO> getAdminStatisticDetailsDTO() {
        return adminStatisticsDetailsDTO;
    }

    public void setAdminStatisticDetailsDTO(List<AdminStatisticsDetailsDTO> adminStatisticsDetailsDTO) {
        this.adminStatisticsDetailsDTO = adminStatisticsDetailsDTO;
    }
}
