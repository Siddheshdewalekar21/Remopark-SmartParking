package com.remopark.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/main-login")
    public String login() {
        return "login";
    }

    @GetMapping("/map")
    public String map(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("roles", authentication.getAuthorities());
            
            // Add sample parking centers (replace with actual data from your database)
            List<Map<String, Object>> parkingCenters = new ArrayList<>();
            parkingCenters.add(Map.of(
                "id", 1,
                "name", "Central Parking",
                "latitude", 18.5204,
                "longitude", 73.8567,
                "price", "₹50/hour",
                "contact", "123-456-7890"
            ));
            
            model.addAttribute("parkingCenters", parkingCenters);
        }
        return "map";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
} 