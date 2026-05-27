package com.example.controller;


import com.example.entity.VehicleTrips;
import com.example.entity.VehicleViolation;
import com.example.service.VehicleViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VehicleViolationController {

    @Autowired
    private VehicleViolationService violationService;

    @PostMapping("/violations")
    public VehicleViolation createViolation(@RequestBody VehicleViolation violation) {
        return violationService.createViolation(violation);
    }

    @GetMapping("/violations")
    public List<VehicleViolation> getAllViolations() {
        return violationService.getAllViolations();
    }

    @GetMapping("/violations/{id}")
    public ResponseEntity<VehicleViolation> getViolationById(@PathVariable int id) {
        VehicleViolation violation = violationService.getViolationById(id);
        return ResponseEntity.ok(violation);
    }

    @PutMapping("/violations/{id}")
    public ResponseEntity<VehicleViolation> updateViolation (
            @PathVariable int id,
            @RequestBody VehicleViolation updatedViolation) {

        VehicleViolation violation = violationService.updateViolationById(id, updatedViolation);

        return ResponseEntity.ok(violation);
    }

    @DeleteMapping("/violations/{id}")
    public void deleteViolation(@PathVariable int id) {
        violationService.deleteViolation(id);
    }

}
