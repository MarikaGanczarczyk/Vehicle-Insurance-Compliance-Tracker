package com.example.service;

import com.example.entity.Vehicle;
import com.example.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private VehicleService service;

    @Test
    public void testToAddVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Ford");

        when(repository.save(vehicle)).thenReturn(vehicle);

        Vehicle result = service.addVehicle(vehicle);

        assertNotNull(result);
        assertEquals("Ford", result.getBrand());
        verify(repository).save(vehicle);
    }

    @Test
    public void getAllVehicles() {
        List<Vehicle> mockList = Arrays.asList(new Vehicle(), new Vehicle());

        when(repository.findAll()).thenReturn(mockList);

        List<Vehicle> result = service.getAllVehicles();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    public void getVehicleById() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1);

        when(repository.findById(1)).thenReturn(Optional.of(vehicle));

        Vehicle result = service.getVehicleById(1);

        assertEquals(1,result.getVehicleId());
        verify(repository).findById(1);
    }


    @Test
    public void getVehicleByIdFailure() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.getVehicleById(1);
        });
        assertEquals("Vehicle not found: 1 ", exception.getMessage());
    }

    @Test
    public void updateVehicleSuccess() {
        int id = 1;

        Vehicle existing = new Vehicle();
        existing.setVehicleId(id);
        existing.setBrand("None");
        existing.setPlate("None123");

        Vehicle updated = new Vehicle();
        updated.setBrand("Toyota");
        updated.setPlate("ABC123");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Vehicle result = service.updateVehicleById(id, updated);

        assertEquals("Toyota", result.getBrand());
        assertEquals("ABC123", result.getPlate());

        verify(repository).findById(id);
        verify(repository).save(existing);
    }

    @Test
    public void updateVehicleByIdFailure() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Vehicle updated = new Vehicle();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.updateVehicleById(1, updated);
                });
        assertEquals("Vehicle not found: 1", exception.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    public void deleteVehicle() {
        int id = 1;

        doNothing().when(repository).deleteById(id);

        service.deleteVehicle(id);

        verify(repository).deleteById(id);
    }
}