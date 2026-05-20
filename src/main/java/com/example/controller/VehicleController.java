package com.example.controller;

import com.example.entity.Vehicle;
import com.example.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Integer id) {
        service.deleteVehicle(id);
    }

}
