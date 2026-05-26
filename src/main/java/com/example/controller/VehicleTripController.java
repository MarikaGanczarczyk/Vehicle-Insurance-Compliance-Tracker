package com.example.controller;

import com.example.entity.VehicleTrips;
import com.example.service.VehicleTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VehicleTripController {

    @Autowired
    private VehicleTripService tripService;

    @PostMapping("/trips")
    public VehicleTrips createTrip(@RequestBody VehicleTrips trip) {
        return tripService.createTrip(trip);
    }

    @GetMapping("/trips")
    public List<VehicleTrips> getAllTrips() {
        return tripService.getAllTrips();
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<VehicleTrips> getTripById(@PathVariable int id) {
        VehicleTrips trips = tripService.getTripById(id);
        return ResponseEntity.ok(trips);
    }

    @PutMapping("/trips/{id}")
    public ResponseEntity<VehicleTrips> updateTrip(
            @PathVariable int id,
            @RequestBody VehicleTrips updatedTrip) {

        VehicleTrips trip = tripService.updateTripById(id, updatedTrip);
        return ResponseEntity.ok(trip);
    }

    @DeleteMapping("/trips/{id}")
    public void deleteTrip(@PathVariable int id) {
        tripService.deleteTrip(id);
    }

}
