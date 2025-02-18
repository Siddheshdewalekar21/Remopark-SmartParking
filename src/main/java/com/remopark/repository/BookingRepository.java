package com.remopark.repository;

import com.remopark.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByParkingCenter_Id(Long parkingCenterId);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.startTime <= :now AND b.endTime >= :now")
    long countActiveBookings(LocalDateTime now);
    
    @Query("SELECT b FROM Booking b WHERE b.parkingCenter.id = :centerId " +
           "AND b.startTime <= :endTime AND b.endTime >= :startTime")
    List<Booking> findOverlappingBookings(Long centerId, LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT DISTINCT b.spotCode FROM Booking b WHERE b.parkingCenter.id = :centerId " +
           "AND b.startTime <= :endTime AND b.endTime >= :startTime")
    Set<String> findBookedSpotCodes(Long centerId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT b FROM Booking b " +
           "JOIN FETCH b.user " +
           "JOIN FETCH b.parkingCenter " +
           "WHERE b.startTime >= :startDate")
    Page<Booking> findAllWithUserAndCenter(LocalDateTime startDate, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN FETCH b.user JOIN FETCH b.parkingCenter " +
           "WHERE b.user.fullName LIKE %:search% OR b.user.email LIKE %:search% OR b.spotCode LIKE %:search%")
    Page<Booking> searchBookings(@Param("search") String search, Pageable pageable);
} 