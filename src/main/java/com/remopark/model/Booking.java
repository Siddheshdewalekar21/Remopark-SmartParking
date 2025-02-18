package com.remopark.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "parking_center_id", nullable = false)
    private ParkingCenter parkingCenter;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration; // in hours
    private String spotCode;
    private String status = "CONFIRMED"; // Default status
    private Double price;  // Add this field
} 