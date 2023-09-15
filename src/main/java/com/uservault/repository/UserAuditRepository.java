package com.uservault.repository;

import com.uservault.model.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAuditRepository extends JpaRepository<UserAudit, UUID> {
    @Query("SELECT ua FROM UserAudit ua " +
            "JOIN ua.user u ON u.userId = ua.user.userId " +
            "WHERE u.email = :email")
    List<UserAudit> findByUserEmail(@Param("email") String email);

    @Query("SELECT ua FROM UserAudit ua " +
            "LEFT JOIN ua.lastUpdatedBy lup " +
            "LEFT JOIN ua.createdBy cb " +
            "WHERE lup.email = :email OR cb.email = :email")
    List<UserAudit> findAllRelatedAuditsByEmail(@Param("email") String email);

    List<UserAudit> findByUserUserId(UUID userId);

    @Query("SELECT ua FROM UserAudit ua " +
            "LEFT JOIN ua.lastUpdatedBy lup " +
            "LEFT JOIN ua.createdBy cb " +
            "WHERE lup.userId = :userId OR cb.userId = :userId")
    List<UserAudit> findAllRelatedAuditsByUserId(@Param("userId") UUID userId);
}

