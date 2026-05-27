package com.example.repository;

import com.example.entity.Vehicle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository repository;

    @Test
    @DisplayName("Testing To Save And Find By Id")
    public void testSaveFindById() {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setPlate("123ABC");
        vehicle.setVehicleId(11);

        Vehicle saved = repository.save(vehicle);

        Optional<Vehicle> result = repository.findById(saved.getVehicleId());

        assertTrue(result.isPresent());
        assertEquals("Toyota", result.get().getBrand());
    }

    @Test
    @DisplayName("Find All Vehicles")
    public void testFindAll() {
        Vehicle v1 = new Vehicle();
        v1.setBrand("Ford");

        Vehicle v2 = new Vehicle();
        v2.setBrand("Honda");

        repository.save(v1);
        repository.save(v2);

        List<Vehicle> vehicles = repository.findAll();

        assertEquals(2,vehicles.size());
    }

    @Test
    @DisplayName("Delete Tested Vehicles")
    public void deleteTestedVehicles() {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("GMC");

        Vehicle saved = repository.save(vehicle);

        repository.deleteById(saved.getVehicleId());

        Optional<Vehicle> result = repository.findById(saved.getVehicleId());

        assertFalse(result.isPresent());
    }

}