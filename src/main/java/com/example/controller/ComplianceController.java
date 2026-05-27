package com.example.controller;

import com.example.entity.VehicleViolation;
import com.example.service.ComplianceService;
import com.example.service.VehicleViolationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compliance")
public class ComplianceController {

    private final ComplianceService complianceService;
    private final VehicleViolationService vehicleViolationService;

    public ComplianceController(
            ComplianceService complianceService,
            VehicleViolationService vehicleViolationService
    ) {
        this.complianceService = complianceService;
        this.vehicleViolationService = vehicleViolationService;
    }

    @PostMapping("/run")
    public List<VehicleViolation> runComplianceCheck() {
        return complianceService.runComplianceCheck();
    }

    @GetMapping("/violations")
    public List<VehicleViolation> getViolations() {
        return vehicleViolationService.getAllViolations();
    }
}
