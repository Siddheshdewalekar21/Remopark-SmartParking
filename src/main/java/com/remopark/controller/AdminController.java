package com.remopark.controller;

import com.remopark.model.User;
import com.remopark.model.ParkingCenter;
import com.remopark.model.ParkingSpot;
import com.remopark.service.UserService;
import com.remopark.service.ParkingService;
import com.remopark.service.BookingService;
import com.remopark.service.RoleService;
import com.remopark.dto.ActivityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public String adminPanel(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("roles", authentication.getAuthorities());
        }
        // Add statistics
        model.addAttribute("userCount", userService.getUserCount());
        model.addAttribute("centerCount", parkingService.getAllCenters().size());
        model.addAttribute("activeBookings", bookingService.getActiveBookingsCount());

        return "admin/dashboard";
    }

    // User Management
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @DeleteMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/users/edit";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User updatedUser) {
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }
        
        // Update the existing user with new values
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setEnabled(updatedUser.isEnabled());
        existingUser.setRoles(updatedUser.getRoles());
        
        userService.updateUser(existingUser);
        return "redirect:/admin/users";
    }

    // Parking Center Management
    @GetMapping("/centers")
    public String manageCenters(Model model) {
        List<ParkingCenter> centers = parkingService.getAllCenters();
        model.addAttribute("centers", centers);
        return "admin/centers";
    }

    @GetMapping("/centers/new")
    public String newCenter(Model model) {
        model.addAttribute("center", new ParkingCenter());
        return "admin/centers/form";
    }

    @PostMapping("/centers")
    public String createCenter(@ModelAttribute ParkingCenter center, RedirectAttributes redirectAttributes) {
        try {
            parkingService.createCenter(center);
            redirectAttributes.addFlashAttribute("message", "Parking center created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating parking center: " + e.getMessage());
        }
        return "redirect:/admin/centers";
    }

    @GetMapping("/centers/{id}/edit")
    public String editCenter(@PathVariable Long id, Model model) {
        model.addAttribute("center", parkingService.getCenterById(id));
        return "admin/centers/form";
    }

    @PostMapping("/centers/{id}")
    public String updateCenter(@PathVariable Long id, @ModelAttribute ParkingCenter center, 
                             RedirectAttributes redirectAttributes) {
        try {
            parkingService.updateCenter(id, center);
            redirectAttributes.addFlashAttribute("message", "Parking center updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating parking center: " + e.getMessage());
        }
        return "redirect:/admin/centers";
    }

    @DeleteMapping("/centers/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCenter(@PathVariable Long id) {
        try {
            parkingService.deleteCenter(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/centers/{id}/spots")
    public String manageParkingSpots(@PathVariable Long id, Model model) {
        ParkingCenter center = parkingService.getCenterById(id);
        List<ParkingSpot> spots = parkingService.getSpotsByCenter(id);
        model.addAttribute("center", center);
        model.addAttribute("spots", spots);
        return "admin/centers/spots";
    }

    @PostMapping("/centers/{centerId}/spots")
    public String addParkingSpot(@PathVariable Long centerId, 
                                @ModelAttribute ParkingSpot spot,
                                RedirectAttributes redirectAttributes) {
        try {
            parkingService.addSpotToCenter(centerId, spot);
            redirectAttributes.addFlashAttribute("message", "Parking spot added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding parking spot: " + e.getMessage());
        }
        return "redirect:/admin/centers/" + centerId + "/spots";
    }

    @DeleteMapping("/centers/spots/{spotId}")
    @ResponseBody
    public ResponseEntity<?> deleteSpot(@PathVariable Long spotId) {
        try {
            parkingService.deleteSpot(spotId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/parking")
    public String parkingDashboard(Model model) {
        // Get all centers with their spots
        List<ParkingCenter> centers = parkingService.getAllCenters();
        model.addAttribute("centers", centers);
        
        // Get total spots count
        long totalSpots = centers.stream()
                .mapToLong(center -> center.getSpots().size())
                .sum();
        model.addAttribute("totalSpots", totalSpots);
        
        // Get available spots count
        long availableSpots = centers.stream()
                .flatMap(center -> center.getSpots().stream())
                .filter(ParkingSpot::isAvailable)
                .count();
        model.addAttribute("availableSpots", availableSpots);
        
        return "admin/parking/dashboard";
    }
} 