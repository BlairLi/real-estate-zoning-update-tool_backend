package com.realestate.service;

import com.realestate.model.Parcel;
import com.realestate.model.ZoningAuditLog;
import com.realestate.repository.ParcelRepository;
import com.realestate.repository.ZoningAuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.ArrayList;

@Service
public class ParcelService {
    private static final Logger logger = LoggerFactory.getLogger(ParcelService.class);

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private ZoningAuditLogRepository auditLogRepository;

    public List<Object[]> getAllParcels() {
        return parcelRepository.findAllParcelsWithGeoJSON();
    }

    @Transactional
    public void updateZoningType(List<Long> parcelIds, String zoningType) {
        logger.info("Starting zoning update for {} parcels to {}", parcelIds.size(), zoningType);
        
        if (parcelIds == null || parcelIds.isEmpty()) {
            throw new IllegalArgumentException("Parcel IDs list cannot be empty");
        }
        if (zoningType == null || zoningType.trim().isEmpty()) {
            throw new IllegalArgumentException("Zoning type cannot be empty");
        }

        List<String> errors = new ArrayList<>();
        int updatedCount = 0;
        
        // Update parcels
        for (Long id : parcelIds) {
            try {
                Parcel parcel = parcelRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Parcel not found: " + id));
                parcel.setZoningType(zoningType);
                parcelRepository.save(parcel);
                updatedCount++;
                logger.debug("Updated parcel {} to zoning type {}", id, zoningType);
            } catch (Exception e) {
                logger.error("Failed to update parcel {}: {}", id, e.getMessage());
                errors.add("Failed to update parcel " + id + ": " + e.getMessage());
            }
        }

        // Create audit log entry
        try {
            ZoningAuditLog auditLog = new ZoningAuditLog();
            auditLog.setActionTimestamp(ZonedDateTime.now());
            auditLog.setActionType("UPDATE_ZONING");
            auditLog.setAffectedParcels(updatedCount);
            auditLog.setNewZoningType(zoningType);
            auditLog.setDetails("Updated " + updatedCount + " parcels to " + zoningType + 
                (errors.isEmpty() ? "" : ". Errors: " + String.join(", ", errors)));
            
            ZoningAuditLog savedLog = auditLogRepository.save(auditLog);
            logger.info("Created audit log entry with ID: {}", savedLog.getId());
        } catch (Exception e) {
            logger.error("Failed to create audit log: {}", e.getMessage());
            throw new RuntimeException("Failed to create audit log", e);
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException("Failed to update some parcels: " + String.join(", ", errors));
        }
        
        logger.info("Successfully completed zoning update for {} parcels", updatedCount);
    }
} 