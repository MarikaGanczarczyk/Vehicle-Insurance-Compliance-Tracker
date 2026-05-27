package com.example.service;

import com.example.entity.VehicleViolation;
import com.example.repository.VehicleViolationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleViolationService {

    @Autowired
    private VehicleViolationRepository repository;

    public VehicleViolation createViolation(VehicleViolation violation) {
        return repository.save(violation);
    }

    public List<VehicleViolation> getAllViolations() {
        return repository.findAllLatestFirst();
    }

    public VehicleViolation getViolationById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Violation" + id));
    }

    public VehicleViolation updateViolationById(int id, VehicleViolation updatedViolation) {
        VehicleViolation existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Violation:" + id));

        existing.setViolationType(updatedViolation.getViolationType());

        return repository.save(existing);
    }

    public void deleteViolation(int id) {
        repository.deleteById(id);
    }

}
