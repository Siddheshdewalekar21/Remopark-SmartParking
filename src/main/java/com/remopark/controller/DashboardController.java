package com.remopark.controller;

import com.remopark.model.User;
import com.remopark.model.Booking;
import com.remopark.service.UserService;
import com.remopark.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.security.Principal;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Optional<User> userOpt = userService.getUserByUsername(principal.getName());
        User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));
        List<Booking> userBookings = bookingService.getBookingsByUserId(user.getId());
        
        model.addAttribute("user", user);
        model.addAttribute("activeBookings", bookingService.getActiveBookingsCount());
        model.addAttribute("totalBookings", userBookings.size());
        model.addAttribute("recentBookings", userBookings.stream()
            .limit(5)
            .toList());
        
        return "dashboard";
    }
} 