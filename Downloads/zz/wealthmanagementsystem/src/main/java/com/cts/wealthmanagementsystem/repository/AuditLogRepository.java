package com.cts.wealthmanagementsystem.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.cts.wealthmanagementsystem.entity.AuditLog;
 
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
}