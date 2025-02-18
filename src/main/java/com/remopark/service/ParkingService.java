package com.remopark.service;

import com.remopark.model.ParkingCenter;
import com.remopark.model.ParkingSpot;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface ParkingService {
    List<ParkingCenter> getAllCenters();
    ParkingCenter getCenterById(Long id);
    ParkingCenter createCenter(ParkingCenter center);
    ParkingCenter updateCenter(Long id, ParkingCenter center);
    void deleteCenter(Long id);
    
    List<ParkingSpot> getSpotsByCenter(Long centerId);
    ParkingSpot addSpotToCenter(Long centerId, ParkingSpot spot);
    void deleteSpot(Long spotId);
    void bookSpot(Long spotId, Long userId);
    Map<LocalTime, List<String>> getAvailableSpots(Long centerId, LocalDate date);
} 