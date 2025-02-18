package com.remopark.controller;

import com.remopark.model.User;
import com.remopark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, 
                             String confirmPassword,
                             RedirectAttributes redirectAttributes) {
        try {
            // Validate password match
            if (!user.getPassword().equals(confirmPassword)) {
                throw new RuntimeException("Passwords do not match");
            }

            // Check if username exists
            if (userService.existsByUsername(user.getUsername())) {
                throw new RuntimeException("Username already exists");
            }

            // Check if email exists
            if (userService.existsByEmail(user.getEmail())) {
                throw new RuntimeException("Email already exists");
            }

            // Create user
            userService.createUser(user);
            
            redirectAttributes.addAttribute("registered", true);
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
} 