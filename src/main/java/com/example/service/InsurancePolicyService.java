package com.example.service;

import com.example.entity.InsurancePolicy;
import com.example.repository.InsurancePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsurancePolicyService {


    @Autowired
    private InsurancePolicyRepository repo;

    public InsurancePolicy addPolicy(InsurancePolicy policy) {
        return repo.save(policy);
    }

    public List<InsurancePolicy> getPolicyByVehicle(Integer vehicleId) {
        return repo.findByVehicle_VehicleId(vehicleId);
    }


    public InsurancePolicy updatePolicy(Integer policyId, InsurancePolicy newPolicy) {
        InsurancePolicy existing = repo.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        existing.setPolicyType(newPolicy.getPolicyType());
        existing.setIssueDate(newPolicy.getIssueDate());
        existing.setExpiryDate(newPolicy.getExpiryDate());
        existing.setVehicle(newPolicy.getVehicle());

        return repo.save(existing);
    }
}


