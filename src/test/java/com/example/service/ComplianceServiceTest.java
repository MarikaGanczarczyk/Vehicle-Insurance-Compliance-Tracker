package com.example.service;

import com.example.entity.InsurancePolicy;
import com.example.entity.Vehicle;
import com.example.entity.VehicleTrips;
import com.example.entity.VehicleViolation;
import com.example.repository.InsurancePolicyRepository;
import com.example.repository.VehicleRepository;
import com.example.repository.VehicleTripsRepository;
import com.example.repository.VehicleViolationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplianceServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @Mock
    private VehicleTripsRepository vehicleTripsRepository;

    @Mock
    private VehicleViolationRepository vehicleViolationRepository;

    @InjectMocks
    private ComplianceService complianceService;

    @Test
    public void testRunComplianceCheckNoVehicles() {
        when(vehicleRepository.findAll()).thenReturn(Collections.emptyList());

        List<VehicleViolation> result = complianceService.runComplianceCheck();

        assertTrue(result.isEmpty());
        verify(vehicleRepository.findAll());
    }

    @Test
    public void testRunComplianceCheckExpiredInsurance() {

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1);

        InsurancePolicy policy = new InsurancePolicy();
        policy.setExpiryDate(LocalDate.now().minusDays(10));

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(insurancePolicyRepository.findPoliciesForVehicle(1))
                .thenReturn(List.of(policy));
        when(vehicleTripsRepository.findTripHistoryByVehicleId(1))
                .thenReturn(Collections.emptyList());

        when(vehicleViolationRepository.existsByVehicleIdAndViolationTypeAndDateTime(
                any(), any(), any()))
                .thenReturn(false);

        List<VehicleViolation> result = complianceService.runComplianceCheck();

        assertEquals(1, result.size());
        assertEquals("Expired_Insurance", result.get(0).getViolationType());

        verify(vehicleViolationRepository).save(any());
    }

    @Test
    public void testRunComplianceCheckUninsured() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1);

        InsurancePolicy policy = new InsurancePolicy();
        policy.setIssueDate(LocalDate.now().minusDays(15));
        policy.setExpiryDate(LocalDate.now().minusDays(5));

        VehicleTrips trip = new VehicleTrips();
        trip.setDateTime(LocalDateTime.now());
        trip.setAction("Driving");
        trip.setLocation("London");

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(insurancePolicyRepository.findPoliciesForVehicle(1))
                .thenReturn(List.of(policy));
        when(vehicleTripsRepository.findTripHistoryByVehicleId(1))
                .thenReturn(List.of(trip));

        when(vehicleViolationRepository.existsByVehicleIdAndViolationTypeAndDateTime(
                any(), any(), any()))
                .thenReturn(false);

        List<VehicleViolation> result = complianceService.runComplianceCheck();

        assertEquals(2, result.size());

        verify(vehicleViolationRepository, times(2)).save(any());
    }

    @Test
    public void testRunComplianceCheckExistingViolations() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1);

        InsurancePolicy policy = new InsurancePolicy();
        policy.setExpiryDate(LocalDate.now().minusDays(1));

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(insurancePolicyRepository.findPoliciesForVehicle(1))
                .thenReturn(List.of(policy));
        when(vehicleTripsRepository.findTripHistoryByVehicleId(1))
                .thenReturn(Collections.emptyList());

        when(vehicleViolationRepository.existsByVehicleIdAndViolationTypeAndDateTime(
                any(), any(), any()))
                .thenReturn(true);

        List<VehicleViolation> result = complianceService.runComplianceCheck();

        assertTrue(result.isEmpty());
        verify(vehicleViolationRepository, never()).save(any());

    }

    @Test
    public void testRunComplianceCheckTripCovered() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1);

        InsurancePolicy policy = new InsurancePolicy();
        policy.setIssueDate(LocalDate.now().minusDays(5));
        policy.setExpiryDate(LocalDate.now().minusDays(5));

        VehicleTrips trips = new VehicleTrips();
        trips.setDateTime(LocalDateTime.now());
        trips.setAction("Driving");
        trips.setLocation("London");

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(insurancePolicyRepository.findPoliciesForVehicle(1))
                .thenReturn(List.of(policy));
        when(vehicleTripsRepository.findTripHistoryByVehicleId(1))
                .thenReturn(List.of(trips));

        List<VehicleViolation> result = complianceService.runComplianceCheck();

        assertTrue(result.isEmpty());
        verify(vehicleViolationRepository, never()).save(any());

    }

}
