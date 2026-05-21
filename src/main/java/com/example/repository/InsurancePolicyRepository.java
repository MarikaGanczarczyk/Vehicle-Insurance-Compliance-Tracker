package com.example.repository;

import com.example.entity.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Integer> {
    List<InsurancePolicy> findByVehicle_VehicleId(Integer vehicleId);
}
