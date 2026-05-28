package com.example.repository;

import com.example.entity.VehicleViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VehicleViolationRepository extends JpaRepository<VehicleViolation, Integer> {

    boolean existsByVehicleIdAndViolationTypeAndDateTime(
            int vehicleId,
            String violationType,
            LocalDateTime dateTime
    );

    // Get violations ordered by latest first
    @Query("SELECT v FROM VehicleViolation v ORDER BY v.dateTime DESC")
    List<VehicleViolation> findAllLatestFirst();

    // Get violations for a specific date (used by CSV)
    @Query("SELECT v FROM VehicleViolation v WHERE CAST(v.dateTime AS date) = :date")
    List<VehicleViolation> findByDate(@Param("date") LocalDate date);
}