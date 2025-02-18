package com.remopark.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "parking_centers")
@Data
public class ParkingCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private double price;
    private double latitude;
    private double longitude;
    private String contact;
    
    @OneToMany(mappedBy = "parkingCenter", cascade = CascadeType.ALL)
    private List<ParkingSpot> spots;
} 