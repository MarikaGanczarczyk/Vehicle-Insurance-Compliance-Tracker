package com.example.service;

import com.example.entity.Vehicle;
import com.example.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository repository;

    public Vehicle addVehicle(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }

    // getVehicleById
    public Vehicle getVehicleById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
    }

    // we need updateVehicleById
    public Vehicle updateVehicleById(int id, Vehicle updatedVehicle) {

        Vehicle existingVehicle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));

        existingVehicle.setBrand(updatedVehicle.getBrand());
        existingVehicle.setPlate(updatedVehicle.getPlate());

        return repository.save(existingVehicle);
    }

    public void deleteVehicle(int id) {
        repository.deleteById(id);
    }



}
