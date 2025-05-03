package com.realestate.dto;

import java.util.List;

public class ZoningUpdateRequest {
    private List<Long> parcelIds;
    private String zoningType;

    // Getters and Setters
    public List<Long> getParcelIds() {
        return parcelIds;
    }

    public void setParcelIds(List<Long> parcelIds) {
        this.parcelIds = parcelIds;
    }

    public String getZoningType() {
        return zoningType;
    }

    public void setZoningType(String zoningType) {
        this.zoningType = zoningType;
    }
} 