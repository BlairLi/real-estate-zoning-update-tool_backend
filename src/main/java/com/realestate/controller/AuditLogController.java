package com.realestate.controller;

import com.realestate.model.ZoningAuditLog;
import com.realestate.repository.ZoningAuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    @Autowired
    private ZoningAuditLogRepository auditLogRepository;

    @GetMapping
    public ResponseEntity<List<ZoningAuditLog>> getAuditLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
            @RequestParam(required = false) String actionType) {
        
        List<ZoningAuditLog> logs;
        
        if (startDate != null && endDate != null) {
            if (actionType != null) {
                logs = auditLogRepository.findByActionTimestampBetweenAndActionTypeOrderByActionTimestampDesc(
                    startDate, endDate, actionType);
            } else {
                logs = auditLogRepository.findByActionTimestampBetweenOrderByActionTimestampDesc(
                    startDate, endDate);
            }
        } else if (actionType != null) {
            logs = auditLogRepository.findByActionTypeOrderByActionTimestampDesc(actionType);
        } else {
            logs = auditLogRepository.findAllByOrderByActionTimestampDesc();
        }
        
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoningAuditLog> getAuditLogById(@PathVariable Long id) {
        return auditLogRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
} 