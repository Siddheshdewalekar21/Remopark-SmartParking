package com.remopark.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "booked_spots")
@Data
public class BookedSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "parking_spot_id")
    private ParkingSpot parkingSpot;
    
    @ManyToOne
    @JoinColumn(name = "parking_center_id")
    private ParkingCenter parkingCenter;
    
    private String spotCode;
    private LocalDateTime bookingTime;
} 