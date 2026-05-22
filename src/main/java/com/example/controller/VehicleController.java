package com.example.controller;

import com.example.entity.Vehicle;
import com.example.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @PostMapping("/vehicles")
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return service.addVehicle(vehicle);
    }

    @GetMapping("/vehicles")
    public List<Vehicle> getAllVehicles() {
        return service.getAllVehicles();

    }

    //GET /api/vehicles/{ID}
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable int id) {

        Vehicle vehicle = service.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    //PUT /api/vehicles/{ID}
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable int id,
            @RequestBody Vehicle updatedVehicle) {

        Vehicle vehicle = service.updateVehicleById(id, updatedVehicle);
        return ResponseEntity.ok(vehicle);
    }

    @DeleteMapping("/vehicles/{id}")
    public void deleteVehicle(@PathVariable Integer id) {
        service.deleteVehicle(id);
    }

}
