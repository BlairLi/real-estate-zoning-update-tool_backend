package com.realestate.controller;

import com.realestate.service.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/parcels")
@CrossOrigin(origins = "http://localhost:3000")
public class ParcelController {
    @Autowired
    private ParcelService parcelService;

    @GetMapping
    public ResponseEntity<List<Object[]>> getAllParcels() {
        return ResponseEntity.ok(parcelService.getAllParcels());
    }

    @PostMapping("/update-zoning")
    public ResponseEntity<?> updateZoningType(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> parcelIdsList = (List<Object>) request.get("parcelIds");
            String zoningType = (String) request.get("zoningType");

            if (parcelIdsList == null || zoningType == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Invalid request: parcelIds and zoningType are required");
                return ResponseEntity.badRequest().body(response);
            }

            // Convert all parcelIds to Long
            List<Long> parcelIds = parcelIdsList.stream()
                .map(id -> {
                    if (id instanceof Number) {
                        return ((Number) id).longValue();
                    } else if (id instanceof String) {
                        return Long.parseLong((String) id);
                    } else {
                        throw new IllegalArgumentException("Invalid parcel ID type: " + id.getClass());
                    }
                })
                .toList();

            parcelService.updateZoningType(parcelIds, zoningType);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Successfully updated zoning types");
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error updating zoning: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 