package com.remopark.service.impl;

import com.remopark.model.Booking;
import com.remopark.repository.BookingRepository;
import com.remopark.service.BookingService;
import com.remopark.dto.BookingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUser_Id(userId);
    }

    @Override
    public List<Booking> getBookingsByParkingCenterId(Long centerId) {
        return bookingRepository.findByParkingCenter_Id(centerId);
    }

    @Override
    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        bookingRepository.delete(booking);
    }

    @Override
    public long getActiveBookingsCount() {
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findAll().stream()
            .filter(booking -> booking.getStartTime().isBefore(now) && 
                             booking.getEndTime().isAfter(now))
            .count();
    }

    @Override
    public Page<BookingDTO> getAllBookingsForAdmin(int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        LocalDateTime startDate = LocalDateTime.now().minusMonths(1); // Show last month's bookings
        Page<Booking> bookings = bookingRepository.findAllWithUserAndCenter(startDate, pageable);
        
        return bookings.map(BookingDTO::fromBooking);
    }

    @Override
    public Page<BookingDTO> searchBookings(String search, int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Booking> bookings = bookingRepository.searchBookings(search, pageable);
        return bookings.map(BookingDTO::fromBooking);
    }
} 