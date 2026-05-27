package com.example.repository;

import com.example.entity.VehicleViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VehicleViolationRepository extends JpaRepository<VehicleViolation, Integer> {
    boolean existsByVehicleIdAndViolationTypeAndDateTime(int vehicleId, String violationType, LocalDateTime dateTime);

    @Query("""
            select violation from VehicleViolation violation
            order by violation.dateTime desc
            """)
    List<VehicleViolation> findAllLatestFirst();
}