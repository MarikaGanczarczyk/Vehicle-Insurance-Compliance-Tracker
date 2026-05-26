package com.example.service;

import com.example.entity.VehicleTrips;
import com.example.repository.VehicleTripsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleTripService {

    @Autowired
    private VehicleTripsRepository repository;

    public VehicleTrips createTrip(VehicleTrips trip) {
        return repository.save(trip);
    }

    public List<VehicleTrips> getAllTrips() {
        return repository.findAll();
    }

    public VehicleTrips getTripById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException
                        ("Trip not found with id: " + id));
    }

    public VehicleTrips updateTripById(int id, VehicleTrips updatedTrip) {

        VehicleTrips existingTrip = repository.findById(id)
                .orElseThrow(() -> new RuntimeException
                        ("Trip not found with id: " + id));

        existingTrip.setLocation(updatedTrip.getLocation());
        existingTrip.setDateTime(updatedTrip.getDateTime());
        existingTrip.setAction(updatedTrip.getAction());

        return  repository.save(existingTrip);

    }

    public void deleteTrip(int id) {
        repository.deleteById(id);
    }

}
