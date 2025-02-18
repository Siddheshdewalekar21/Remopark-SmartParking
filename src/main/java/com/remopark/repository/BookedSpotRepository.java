package com.remopark.repository;

import com.remopark.model.BookedSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookedSpotRepository extends JpaRepository<BookedSpot, Long> {
    boolean existsByParkingSpotIdAndBookingTime(Long spotId, LocalDateTime bookingTime);
    List<BookedSpot> findByUserUsername(String username);
} 