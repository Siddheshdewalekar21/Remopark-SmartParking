package com.remopark.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "parking_spots")
@Data
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String spotCode;
    
    private boolean available = true;
    
    @ManyToOne
    @JoinColumn(name = "parking_center_id")
    private ParkingCenter parkingCenter;
} 