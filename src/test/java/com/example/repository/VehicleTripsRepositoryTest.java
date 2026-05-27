package com.example.repository;

import com.example.entity.VehicleTrips;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VehicleTripsRepositoryTest {

    @Autowired
    private VehicleTripsRepository repository;

    @Test
    @DisplayName("Test To Save And Find Trip By ID")
    public void testSaveAndFindById() {

        VehicleTrips trip = new VehicleTrips();
        trip.setLocation("London");
        trip.setVehicleId(1);
        trip.setAction("Driving");

        VehicleTrips savedTrip = repository.save(trip);

        Optional<VehicleTrips> result = repository.findById(savedTrip.getVehicleId());

        assertTrue(result.isPresent());
        assertEquals("London", result.get().getLocation());
        assertEquals("Driving", result.get().getAction());
    }

    @Test
    @DisplayName("Find All Trips")
    public void testFindAllTrips() {
        VehicleTrips trip1 = new VehicleTrips();
        trip1.setLocation("Arizona");

        VehicleTrips trip2 = new VehicleTrips();
        trip2.setLocation("California");

        repository.save(trip1);
        repository.save(trip2);

        List<VehicleTrips> trips = repository.findAll();
        assertEquals(2, trips.size());
    }

    @Test
    @DisplayName("Delete Trip")
    public void testToDeleteTrip() {
        VehicleTrips trip = new VehicleTrips();
        trip.setLocation("Test");

        VehicleTrips savedTrip = repository.save(trip);

        repository.deleteById(savedTrip.getId());

        Optional<VehicleTrips> result = repository.findById(savedTrip.getId());

        assertFalse(result.isPresent());
    }

}