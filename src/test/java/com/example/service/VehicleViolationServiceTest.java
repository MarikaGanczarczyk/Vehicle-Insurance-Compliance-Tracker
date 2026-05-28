package com.example.service;

import com.example.entity.VehicleViolation;
import com.example.repository.VehicleViolationRepository;
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
class VehicleViolationServiceTest {

    @Mock
    private VehicleViolationRepository repository;

    @InjectMocks
    private VehicleViolationService service;

    @Test
    public void testCreateViolation() {
        VehicleViolation violation = new VehicleViolation();
        violation.setViolationType("Expired");

        when(repository.save(violation)).thenReturn(violation);

        VehicleViolation result = service.createViolation(violation);

        assertNotNull(result);
        assertEquals("Expired", result.getViolationType());
        verify(repository).save(violation);
    }

    @Test
    public void testGetAllViolations(){
        List<VehicleViolation> list = List.of(new VehicleViolation(), new VehicleViolation());

        when(repository.findAll()).thenReturn(list);

        List<VehicleViolation> result = service.getAllViolations();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    public void getViolationById() {
        VehicleViolation violation = new VehicleViolation();
        violation.setVehicleId(1);

        when(repository.findById(1)).thenReturn(Optional.of(violation));

        VehicleViolation result = service.getViolationById(1);

        assertNotNull(result);
    }

    @Test
    public void getViolationByIdFail() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getViolationById(1));

        assertEquals("Violation 1", exception.getMessage());
    }

    @Test
    public void testUpdateViolationById() {
        VehicleViolation existing = new VehicleViolation();
        existing.setVehicleId(1);
        existing.setViolationType("Unpaid");

        VehicleViolation updated = new VehicleViolation();
        updated.setViolationType("Paid");

        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        VehicleViolation result = service.updateViolationById(1, updated);

        assertEquals("Paid", result.getViolationType());
        verify(repository).save(existing);
    }

    @Test
    public void updateViolationByIdFail() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.updateViolationById(1, new VehicleViolation()));

        assertEquals("Violation: 1", exception.getMessage());
    }

    @Test
    public void deleteViolation() {
        doNothing().when(repository).deleteById(1);

        service.deleteViolation(1);

        verify(repository).deleteById(1);
    }
}