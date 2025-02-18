package com.remopark.controller;

import com.remopark.dto.BookingDTO;
import com.remopark.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public String listBookings(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Page<BookingDTO> bookings = search.isEmpty() 
            ? bookingService.getAllBookingsForAdmin(page, size, sortBy, sortDir)
            : bookingService.searchBookings(search, page, size, sortBy, sortDir);

        model.addAttribute("bookings", bookings.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookings.getTotalPages());
        model.addAttribute("totalItems", bookings.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("search", search);

        return "admin/bookings/list";
    }
} 