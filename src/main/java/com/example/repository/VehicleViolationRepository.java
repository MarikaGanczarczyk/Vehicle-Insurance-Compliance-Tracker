package com.example.repository;

import com.example.entity.VehicleViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleViolationRepository extends JpaRepository<VehicleViolation, Integer> {
}
