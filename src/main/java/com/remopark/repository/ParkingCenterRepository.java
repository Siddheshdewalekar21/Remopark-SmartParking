package com.remopark.repository;

import com.remopark.model.ParkingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingCenterRepository extends JpaRepository<ParkingCenter, Long> {
} 