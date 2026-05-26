package com.example.repository;

import com.example.entity.VehicleTrips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTripsRepository extends JpaRepository<VehicleTrips, Integer> {
}
