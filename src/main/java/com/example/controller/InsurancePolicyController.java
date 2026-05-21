package com.example.controller;

import com.example.entity.InsurancePolicy;
import com.example.service.InsurancePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InsurancePolicyController {

    @Autowired
    private InsurancePolicyService service;

    @GetMapping("/policies/{vehicleId}")
    public List<InsurancePolicy> getPolicyByVehicle(@PathVariable Integer vehicleId){
        return service.getPolicyByVehicle( vehicleId);
    }

    @PostMapping("/policies")
    public ResponseEntity<InsurancePolicy> addPolicy(@RequestBody InsurancePolicy policy){

        InsurancePolicy createdPolicy = service.addPolicy(policy);
        return  ResponseEntity.status(HttpStatus.CREATED).body(createdPolicy);
    }

    @PutMapping("/policies/{policyId}")
    public ResponseEntity<InsurancePolicy>  updatePolicy(@PathVariable Integer policyId, @RequestBody InsurancePolicy policy){
        InsurancePolicy updatedPolicy = service.updatePolicy(policyId, policy);
        return ResponseEntity.ok(updatedPolicy);
    }
}
