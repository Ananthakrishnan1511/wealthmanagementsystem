package com.cts.wealthmanagementsystem.entity;
 
import java.sql.Timestamp;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
 
@Entity
@Data
public class AuditLog {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer logId;
  private Timestamp eventTimeStamp;
  private String eventDescription;
  private Integer userId;
  private String complianceStatus;
  
  
}
 