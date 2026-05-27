package com.example.repository;

import com.example.entity.VehicleViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VehicleViolationRepositoryTest {

    @Autowired
    private VehicleViolationRepository repository;

    @Test
    @DisplayName("Find Vehicle Violation By Id")
    public void testSaveAndFindById() {
        VehicleViolation violation = new VehicleViolation();
        violation.setViolationType("Expired");
        violation.setVehicleId(1);
        violation.setDescription("Driving with expired policy");

        VehicleViolation saved = repository.save(violation);

        Optional<VehicleViolation> result = repository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Expired", result.get().getViolationType());
        assertEquals("Driving with expired policy", result.get().getDescription());
    }

    @Test
    @DisplayName("Find All Violations")
    public void testToFindAllViolations() {

        VehicleViolation v1 = new VehicleViolation();
        v1.setViolationType("Expired");

        VehicleViolation v2 = new VehicleViolation();
        v1.setViolationType("Payment Due");

        repository.save(v1);
        repository.save(v2);

        List<VehicleViolation> violations = repository.findAll();

        assertEquals(2,violations.size());
    }

    @Test
    @DisplayName("Deleting Violations")
    public void testDeletingOfViolations() {
        VehicleViolation violation = new VehicleViolation();
        violation.setViolationType("Test");

        VehicleViolation saved = repository.save(violation);

        repository.deleteById(saved.getId());

        Optional<VehicleViolation> result = repository.findById(saved.getId());

        assertFalse(result.isPresent());
    }

}