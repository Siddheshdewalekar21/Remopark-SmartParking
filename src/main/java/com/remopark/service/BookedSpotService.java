package com.remopark.service;

import com.remopark.model.BookedSpot;
import java.util.List;

public interface BookedSpotService {
    List<BookedSpot> getBookedSpotsByUsername(String username);
    BookedSpot bookSpot(Long spotId, String username);
    void cancelBooking(Long bookingId);
} 