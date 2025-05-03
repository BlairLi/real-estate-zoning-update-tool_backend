package com.realestate.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "zoning_audit_log")
public class ZoningAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action_timestamp", nullable = false)
    private ZonedDateTime actionTimestamp;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "affected_parcels", nullable = false)
    private Integer affectedParcels;

    @Column(name = "new_zoning_type", nullable = false)
    private String newZoningType;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getActionTimestamp() {
        return actionTimestamp;
    }

    public void setActionTimestamp(ZonedDateTime actionTimestamp) {
        this.actionTimestamp = actionTimestamp;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getAffectedParcels() {
        return affectedParcels;
    }

    public void setAffectedParcels(Integer affectedParcels) {
        this.affectedParcels = affectedParcels;
    }

    public String getNewZoningType() {
        return newZoningType;
    }

    public void setNewZoningType(String newZoningType) {
        this.newZoningType = newZoningType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
} 