/*package com.example.service;

import com.example.entity.InsurancePolicy;
import com.example.repository.InsurancePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsurancePolicyService {


    @Autowired
    private InsurancePolicyRepository repo;

    public InsurancePolicy addPolicy(InsurancePolicy policy) {
        return repo.save(policy);
    }

    public InsurancePolicy getPolicyByVehicle(Integer vehicleId) {
        return repo.findAll()
                .stream()
                .filter(p -> p.getVehicle().getId().equals(vehicleId))
                .findFirst()
                .orElse(null);
    }

}


 */