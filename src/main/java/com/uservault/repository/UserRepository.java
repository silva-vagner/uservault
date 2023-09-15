package com.uservault.repository;

import com.uservault.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Page<User> findAll(Pageable pageable);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(UUID uuid);
    @Query("SELECT u FROM User u " +
            "JOIN u.authorities role " +
            "WHERE role.authority = :roleName")
    List<User> findByUserRole(@Param("roleName") String roleName);
}

