package com.remopark.controller;

import com.remopark.model.Booking;
import com.remopark.model.ParkingCenter;
import com.remopark.model.User;
import com.remopark.service.BookingService;
import com.remopark.service.ParkingService;
import com.remopark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.security.Principal;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private UserService userService;

    @GetMapping("/create")
    public String showCreateForm(@RequestParam Long centerId, Model model) {
        ParkingCenter center = parkingService.getCenterById(centerId);
        model.addAttribute("center", center);
        
        // Add available times (6 AM to 10 PM)
        List<String> availableTimes = new ArrayList<>();
        for (int hour = 6; hour <= 22; hour++) {
            availableTimes.add(String.format("%02d:00", hour));
        }
        model.addAttribute("availableTimes", availableTimes);
        
        return "bookings/create";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long centerId,
                              @RequestParam String date,
                              @RequestParam String time,
                              @RequestParam Integer duration,
                              @RequestParam String spotCode,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOpt = userService.getUserByUsername(authentication.getName());
            User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));
            ParkingCenter center = parkingService.getCenterById(centerId);
            
            // Parse date and time
            LocalDateTime startTime = LocalDateTime.parse(date + "T" + time);
            LocalDateTime endTime = startTime.plusHours(duration);
            
            // Create booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setParkingCenter(center);
            booking.setSpotCode(spotCode);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setDuration(duration);
            booking.setPrice(center.getPrice() * duration);
            
            bookingService.createBooking(booking);
            
            redirectAttributes.addFlashAttribute("message", "Booking created successfully!");
            return "redirect:/bookings";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating booking: " + e.getMessage());
            return "redirect:/bookings/create?centerId=" + centerId;
        }
    }

    @GetMapping
    public String listBookings(Model model, Principal principal) {
        Optional<User> userOpt = userService.getUserByUsername(principal.getName());
        User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("bookings", bookingService.getBookingsByUserId(user.getId()));
        return "bookings/list";
    }
} 