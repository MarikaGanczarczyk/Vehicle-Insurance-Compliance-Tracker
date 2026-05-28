package com.example.service;

import com.example.entity.VehicleTrips;
import com.example.repository.VehicleTripsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleTripServiceTest {


    @Mock
    private VehicleTripsRepository repository;

    @InjectMocks
    private VehicleTripService service;

    @Test
    public void testCreateTrip() {
        VehicleTrips trip = new VehicleTrips();
        trip.setLocation("London");

        when(repository.save(trip)).thenReturn(trip);

        VehicleTrips result = service.createTrip(trip);

        assertNotNull(result);
        assertEquals("London", result.getLocation());
        verify(repository, times(1)).save(trip);
    }

    @Test
    public void testGetAllTrips() {
        List<VehicleTrips> trip = List.of(new VehicleTrips(), new VehicleTrips());

        when(repository.findAll()).thenReturn(trip);

        List<VehicleTrips> result = service.getAllTrips();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetTripsById() {

        VehicleTrips trip = new VehicleTrips();
        trip.setVehicleId(1);

        when(repository.findById(1)).thenReturn(Optional.of(trip));

        VehicleTrips result = service.getTripById(1);

        assertNotNull(result);
    }

    @Test
    public void testGetTripByIdFail() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getTripById(1));

        assertEquals("Trip not found with Id: 1", exception.getMessage());
    }

    @Test
    public void testUpdateTripById() {
        VehicleTrips existingTrip = new VehicleTrips();
        existingTrip.setVehicleId(1);
        existingTrip.setLocation("Old Location");

        VehicleTrips updatedTrip = new VehicleTrips();
        updatedTrip.setLocation("Madrid");

        when(repository.findById(1)).thenReturn(Optional.of(existingTrip));
        when(repository.save(any(VehicleTrips.class))).thenReturn(existingTrip);

        VehicleTrips result = service.updateTripById(1, updatedTrip);

        assertEquals("Madrid", result.getLocation());
        verify(repository).save(existingTrip);
    }

    @Test
    public void testUpdateTripByIdFail() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.updateTripById(1, new VehicleTrips()));

        assertEquals("Trip not found with id: 1", exception.getMessage());
    }

    @Test
    public void testDeleteTrip() {
        doNothing().when(repository).deleteById(1);

        service.deleteTrip(1);

        verify(repository, times(1)).deleteById(1);
    }
}