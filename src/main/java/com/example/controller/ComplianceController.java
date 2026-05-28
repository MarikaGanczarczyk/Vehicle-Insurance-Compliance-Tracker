package com.example.controller;

import com.example.entity.VehicleViolation;
import com.example.repository.VehicleViolationRepository;
import com.example.service.ComplianceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compliance")
public class ComplianceController {


    private  ComplianceService complianceService;
    private VehicleViolationRepository vehicleViolationRepository;

    public ComplianceController(ComplianceService complianceService, VehicleViolationRepository vehicleViolationRepository) {
        this.complianceService = complianceService;
        this.vehicleViolationRepository = vehicleViolationRepository;
    }

   // this end point is to run Compliance Check
    @PostMapping("/run")
    public List<VehicleViolation> runCompliance() {
        return complianceService.runComplianceCheck();
    }

    // this is to check violation and get all violation 
    @GetMapping("/violations")
    public List<VehicleViolation> getAllViolations() {
        return vehicleViolationRepository.findAll();
    }
}
