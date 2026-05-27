package com.example.repository;

import com.example.entity.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Integer> {
    List<InsurancePolicy> findByVehicle_VehicleId(Integer vehicleId);

    @Query("""
            select policy from InsurancePolicy policy
            where policy.vehicle.vehicleId = :vehicleId
            order by policy.issueDate desc, policy.expiryDate desc
            """)
    List<InsurancePolicy> findPoliciesForVehicle(@Param("vehicleId") Integer vehicleId);
}
