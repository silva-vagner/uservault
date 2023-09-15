package com.uservault.controller;

import com.uservault.dto.user.AdminAuditDTO;
import com.uservault.dto.user.UserAuditDTO;
import com.uservault.exception.UserReferenceMissingException;
import com.uservault.service.UserAuditService;
import com.uservault.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@Tag(name = "Audit", description = "Recursos exclusivos para auditores.")
@RequestMapping("/audit")
public class AuditController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuditService userAuditService;

    @PreAuthorize("hasRole('AUDITOR')")
    @Operation(description = "Busca informações de auditoria do usuário.")
    @GetMapping("/users")
    public ResponseEntity<?> getUserAudit(@RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "user_id", required = false) UUID userId) {
        if(email == null && userId == null){
            throw new UserReferenceMissingException();
        }
        List<UserAuditDTO> userAudit = userAuditService.getUserAudit(email, userId);
        return ResponseEntity.ok(userAudit);
    }

    @PreAuthorize("hasRole('AUDITOR')")
    @Operation(description = "Busca informações de auditoria do admin.")
    @GetMapping("/admin")
    public ResponseEntity<?> getAdminAudit(@RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "user_id", required = false) UUID userId) {
        if(email == null && userId == null){
            throw new UserReferenceMissingException();
        }
        AdminAuditDTO userAudit = userAuditService.getAdminAuditByEmailOrId(email, userId);
        return ResponseEntity.ok(userAudit);
    }
}
