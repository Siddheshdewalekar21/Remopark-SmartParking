package com.remopark.service;

import com.remopark.model.Booking;
import com.remopark.dto.BookingDTO;
import org.springframework.data.domain.Page;
import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);
    Booking getBookingById(Long id);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByUserId(Long userId);
    List<Booking> getBookingsByParkingCenterId(Long centerId);
    void cancelBooking(Long id);
    long getActiveBookingsCount();
    Page<BookingDTO> getAllBookingsForAdmin(int page, int size, String sortBy, String sortDir);
    Page<BookingDTO> searchBookings(String search, int page, int size, String sortBy, String sortDir);
} 