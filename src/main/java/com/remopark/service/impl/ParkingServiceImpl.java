package com.remopark.service.impl;

import com.remopark.model.ParkingCenter;
import com.remopark.model.ParkingSpot;
import com.remopark.repository.BookingRepository;
import com.remopark.repository.ParkingCenterRepository;
import com.remopark.repository.ParkingSpotRepository;
import com.remopark.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ParkingServiceImpl implements ParkingService {
    
    @Autowired
    private ParkingCenterRepository parkingCenterRepository;
    
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<ParkingCenter> getAllCenters() {
        return parkingCenterRepository.findAll();
    }

    @Override
    public ParkingCenter getCenterById(Long id) {
        return parkingCenterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parking center not found"));
    }

    @Override
    public ParkingCenter createCenter(ParkingCenter center) {
        return parkingCenterRepository.save(center);
    }

    @Override
    public ParkingCenter updateCenter(Long id, ParkingCenter center) {
        ParkingCenter existingCenter = getCenterById(id);
        existingCenter.setName(center.getName());
        existingCenter.setPrice(center.getPrice());
        existingCenter.setLatitude(center.getLatitude());
        existingCenter.setLongitude(center.getLongitude());
        existingCenter.setContact(center.getContact());
        return parkingCenterRepository.save(existingCenter);
    }

    @Override
    public void deleteCenter(Long id) {
        parkingCenterRepository.deleteById(id);
    }

    @Override
    public List<ParkingSpot> getSpotsByCenter(Long centerId) {
        return parkingSpotRepository.findByParkingCenterId(centerId);
    }

    @Override
    public ParkingSpot addSpotToCenter(Long centerId, ParkingSpot spot) {
        ParkingCenter center = getCenterById(centerId);
        spot.setParkingCenter(center);
        return parkingSpotRepository.save(spot);
    }

    @Override
    public void deleteSpot(Long spotId) {
        parkingSpotRepository.deleteById(spotId);
    }

    @Override
    public void bookSpot(Long spotId, Long userId) {
        // Implementation for booking a spot
        // This can be implemented based on your requirements
    }

    @Override
    public Map<LocalTime, List<String>> getAvailableSpots(Long centerId, LocalDate date) {
        Map<LocalTime, List<String>> availabilityMap = new HashMap<>();
        List<String> allSpots = Arrays.asList("A1", "A2", "A3", "A4", "A5", "A6");
        
        for (int hour = 6; hour <= 22; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            LocalDateTime startTime = LocalDateTime.of(date, time);
            LocalDateTime endTime = startTime.plusHours(1);
            
            Set<String> bookedSpots = bookingRepository.findBookedSpotCodes(centerId, startTime, endTime);
            
            List<String> availableSpots = new ArrayList<>(allSpots);
            availableSpots.removeAll(bookedSpots);
            
            if (!availableSpots.isEmpty()) {
                availabilityMap.put(time, availableSpots);
            }
        }
        
        return availabilityMap;
    }
} 