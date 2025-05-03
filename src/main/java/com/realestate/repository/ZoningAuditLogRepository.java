package com.realestate.repository;

import com.realestate.model.ZoningAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ZoningAuditLogRepository extends JpaRepository<ZoningAuditLog, Long> {
    List<ZoningAuditLog> findByActionTimestampBetweenAndActionTypeOrderByActionTimestampDesc(
        ZonedDateTime startDate, ZonedDateTime endDate, String actionType);
    
    List<ZoningAuditLog> findByActionTimestampBetweenOrderByActionTimestampDesc(
        ZonedDateTime startDate, ZonedDateTime endDate);
    
    List<ZoningAuditLog> findByActionTypeOrderByActionTimestampDesc(String actionType);
    
    List<ZoningAuditLog> findAllByOrderByActionTimestampDesc();
} 