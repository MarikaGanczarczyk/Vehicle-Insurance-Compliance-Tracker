package com.example.repository;

import com.example.entity.InsurancePolicy;
import com.example.entity.Vehicle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InsurancePolicyRepositoryTest {

    @Autowired
    private InsurancePolicyRepository repository;

    @Test
    @DisplayName("find By Vehicle_id")
    void testByVehicleVehicleId() {

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(1);
        vehicle.setPlate("ABC123");
        vehicle.setBrand("Toyota");

        InsurancePolicy policy1 = new InsurancePolicy();
        policy1.setId(11111);
        policy1.setVehicle(vehicle);

        InsurancePolicy policy2 = new InsurancePolicy();
        policy2.setId(2222);
        policy2.setVehicle(vehicle);

        repository.save(policy1);
        repository.save(policy2);

        List<InsurancePolicy> result = repository.findByVehicle_VehicleId(1);

        assertNotNull(result);
        assertEquals(2,result.size());

    }
}