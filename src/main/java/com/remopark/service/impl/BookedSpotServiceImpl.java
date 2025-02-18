package com.remopark.service.impl;

import com.remopark.model.BookedSpot;
import com.remopark.model.User;
import com.remopark.repository.BookedSpotRepository;
import com.remopark.repository.UserRepository;
import com.remopark.service.BookedSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookedSpotServiceImpl implements BookedSpotService {
    
    @Autowired
    private BookedSpotRepository bookedSpotRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<BookedSpot> getBookedSpotsByUsername(String username) {
        return bookedSpotRepository.findByUserUsername(username);
    }
    
    @Override
    public BookedSpot bookSpot(Long spotId, String username) {
        // Implementation for booking a spot
        // TODO: Add implementation
        return null;
    }
    
    @Override
    public void cancelBooking(Long bookingId) {
        bookedSpotRepository.deleteById(bookingId);
    }
} 