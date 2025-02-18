package com.remopark.controller;

import com.remopark.model.Booking;
import com.remopark.model.ParkingCenter;
import com.remopark.repository.ParkingCenterRepository;
import com.remopark.repository.BookingRepository;
import com.remopark.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
public class ParkingCenterController {

    @Autowired
    private ParkingCenterRepository parkingCenterRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingService parkingService;

    @GetMapping("/centers/{id}")
    public String viewCenter(@PathVariable Long id, Model model) {
        ParkingCenter center = parkingCenterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parking center not found"));
        
        // Get all bookings for this center
        List<Booking> bookings = bookingRepository.findByParkingCenter_Id(id);
        
        // Convert bookings to a format that's easy to use in JavaScript
        List<Map<String, String>> bookedSpots = bookings.stream()
            .map(booking -> {
                Map<String, String> spotInfo = new HashMap<>();
                spotInfo.put("date", booking.getStartTime().toLocalDate().toString());
                spotInfo.put("time", booking.getStartTime().toLocalTime().toString());
                spotInfo.put("spotCode", booking.getSpotCode());
                spotInfo.put("status", "CONFIRMED"); // Since we're not using status anymore
                return spotInfo;
            })
            .collect(Collectors.toList());
        
        model.addAttribute("center", center);
        model.addAttribute("bookedSpots", bookedSpots);
        return "center-details";
    }
    
    @GetMapping("/api/centers/{id}/availability")
    @ResponseBody
    public Map<LocalTime, List<String>> getAvailability(
            @PathVariable Long id,
            @RequestParam LocalDate date) {
        return parkingService.getAvailableSpots(id, date);
    }
} 