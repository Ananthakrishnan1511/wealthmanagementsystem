package com.cts.wealthmanagementsystem.service;

import java.util.List;
import java.util.Optional;

import com.cts.wealthmanagementsystem.entity.AuditLog;

public interface AuditLogService {
    public AuditLog verifyCompliance(String status);
    public Optional<AuditLog> getAuditLog(Integer id);
    public AuditLog logAuditEvent(AuditLog auditLog);
    
}
