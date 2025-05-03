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
import java.util.stream.Collectors;

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
            String errorMessage = "Parcel IDs list cannot be empty";
            createAuditLog("UPDATE_ZONING_ERROR", 0, zoningType, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (zoningType == null || zoningType.trim().isEmpty()) {
            String errorMessage = "Zoning type cannot be empty";
            createAuditLog("UPDATE_ZONING_ERROR", 0, zoningType, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Check if any parcels already have the target zoning type
        List<Parcel> parcels = parcelRepository.findAllById(parcelIds);
        List<Long> alreadyUpdatedParcels = parcels.stream()
            .filter(p -> zoningType.equals(p.getZoningType()))
            .map(Parcel::getId)
            .collect(Collectors.toList());

        if (!alreadyUpdatedParcels.isEmpty()) {
            String errorMessage = "The following parcels already have zoning type '" + zoningType + "': " + 
                alreadyUpdatedParcels.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            
            createAuditLog("UPDATE_ZONING_ERROR", alreadyUpdatedParcels.size(), zoningType, errorMessage);
            throw new IllegalArgumentException(errorMessage);
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

        // Create audit log entry for the update attempt
        String details = "Updated " + updatedCount + " parcels to " + zoningType;
        if (!errors.isEmpty()) {
            details += ". Errors: " + String.join(", ", errors);
        }
        
        createAuditLog(
            errors.isEmpty() ? "UPDATE_ZONING_SUCCESS" : "UPDATE_ZONING_PARTIAL",
            updatedCount,
            zoningType,
            details
        );

        if (!errors.isEmpty()) {
            throw new RuntimeException("Failed to update some parcels: " + String.join(", ", errors));
        }
        
        logger.info("Successfully completed zoning update for {} parcels", updatedCount);
    }

    private void createAuditLog(String actionType, int affectedParcels, String zoningType, String details) {
        try {
            ZoningAuditLog auditLog = new ZoningAuditLog();
            auditLog.setActionTimestamp(ZonedDateTime.now());
            auditLog.setActionType(actionType);
            auditLog.setAffectedParcels(affectedParcels);
            auditLog.setNewZoningType(zoningType);
            auditLog.setDetails(details);
            
            ZoningAuditLog savedLog = auditLogRepository.save(auditLog);
            logger.info("Created audit log entry with ID: {} for action type: {}", savedLog.getId(), actionType);
        } catch (Exception e) {
            logger.error("Failed to create audit log: {}", e.getMessage());
            throw new RuntimeException("Failed to create audit log", e);
        }
    }
} 