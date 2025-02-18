package com.remopark.controller;

import com.remopark.model.ParkingCenter;
import com.remopark.model.ParkingSpot;
import com.remopark.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    
    @Autowired
    private ParkingService parkingService;
    
    @GetMapping("/centers")
    public ResponseEntity<List<ParkingCenter>> getAllCenters() {
        return ResponseEntity.ok(parkingService.getAllCenters());
    }
    
    @GetMapping("/centers/{id}/spots")
    public ResponseEntity<List<ParkingSpot>> getCenterSpots(@PathVariable Long id) {
        return ResponseEntity.ok(parkingService.getSpotsByCenter(id));
    }
    
    @PostMapping("/spots/{spotId}/book")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> bookSpot(@PathVariable Long spotId) {
        // Get the current user's ID from the security context
        // This is a simplified version - you'll need to implement proper user handling
        Long userId = 1L; // Placeholder
        parkingService.bookSpot(spotId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/centers/{id}/availability")
    public ResponseEntity<Map<LocalTime, List<String>>> getAvailability(
            @PathVariable Long id,
            @RequestParam LocalDate date) {
        return ResponseEntity.ok(parkingService.getAvailableSpots(id, date));
    }
} 