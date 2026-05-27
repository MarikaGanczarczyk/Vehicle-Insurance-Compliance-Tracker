package com.example.repository;

import com.example.entity.VehicleTrips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleTripsRepository extends JpaRepository<VehicleTrips, Integer> {
    @Query("""
            select trip from VehicleTrips trip
            where trip.vehicleId = :vehicleId
            order by trip.dateTime asc
            """)
    List<VehicleTrips> findTripHistoryByVehicleId(@Param("vehicleId") int vehicleId);
}
