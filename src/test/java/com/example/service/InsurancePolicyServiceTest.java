package com.example.service;

import com.example.entity.InsurancePolicy;
import com.example.entity.Vehicle;
import com.example.repository.InsurancePolicyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyServiceTest {

    @Mock
    private InsurancePolicyRepository repo;

    @InjectMocks
    private InsurancePolicyService service;

    @Test
    public void testAddPolicy() {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setPolicyType("Full Coverage");

        when(repo.save(policy)).thenReturn(policy);

        InsurancePolicy result = service.addPolicy(policy);

        assertNotNull(result);
        assertEquals("Full Coverage", result.getPolicyType());
        verify(repo, times(1)).save(policy);
    }

    @Test
    public void testGetPolicyByVehicle() {
        int vehicleId = 1;

        InsurancePolicy policy1 = new InsurancePolicy();
        InsurancePolicy policy2 = new InsurancePolicy();

        List<InsurancePolicy> mockList = Arrays.asList(policy1, policy2);

        when(repo.findByVehicle_VehicleId(vehicleId)).thenReturn(mockList);

        List<InsurancePolicy> result = service.getPolicyByVehicle(vehicleId);

        assertEquals(2, result.size());
        verify(repo, times(1)).findByVehicle_VehicleId(vehicleId);
    }

    @Test
    public void testUpdatePolicySuccess() {
        int policyId = 1;

        Vehicle vehicle = new Vehicle();

        InsurancePolicy existing = new InsurancePolicy();
        existing.setPolicyType("Expired");
        existing.setVehicle(vehicle);

        InsurancePolicy newPolicy = new InsurancePolicy();
        newPolicy.setPolicyType("New");
        newPolicy.setIssueDate(LocalDate.now());
        newPolicy.setExpiryDate(LocalDate.now().plusDays(30));
        newPolicy.setVehicle(vehicle);

        when(repo.findById(policyId)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        InsurancePolicy result = service.updatePolicy(policyId, newPolicy);

        assertEquals("New", result.getPolicyType());
        verify(repo).findById(policyId);
        verify(repo).save(existing);
    }

    @Test
    public void testUpdatePolicyMissing() {
        int policyId = 1;
        InsurancePolicy newPolicy = new InsurancePolicy();

        when(repo.findById(policyId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.updatePolicy(policyId, newPolicy);
        });

        assertEquals("Policy not found", exception.getMessage());
        verify(repo).findById(policyId);
        verify(repo, never()).save(any());
    }
}