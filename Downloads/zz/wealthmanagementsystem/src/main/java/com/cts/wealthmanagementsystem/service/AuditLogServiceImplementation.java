package com.cts.wealthmanagementsystem.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.wealthmanagementsystem.entity.AuditLog;
import com.cts.wealthmanagementsystem.repository.AuditLogRepository;

@Service
public class AuditLogServiceImplementation implements AuditLogService{

	@Autowired
	public AuditLogRepository auditLogRepository;
	
	
	@Override
	public AuditLog verifyCompliance(String status) {
		  AuditLog log = new AuditLog();
          log.setEventTimeStamp(Timestamp.from(Instant.now()));
         
          log.setUserId(0); 
          if(status.equals("FAILED")) {
        	  log.setComplianceStatus("FAILED");
        	  log.setEventDescription("User has chosen wrong risk appetite plan");
          }
          else {
        	  log.setComplianceStatus("PASSED");
        	  log.setEventDescription("User has chosen right risk appetite plan");
          }
          
		return log;
		
	}

	@Override
	public Optional<AuditLog> getAuditLog(Integer id) {
		
		return auditLogRepository.findById(id);
	}

	@Override
	public AuditLog logAuditEvent(AuditLog auditLog) {
		
		return auditLogRepository.save(auditLog);
	}

}
