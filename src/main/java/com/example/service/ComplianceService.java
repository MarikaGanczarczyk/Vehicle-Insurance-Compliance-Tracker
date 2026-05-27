package com.example.service;

import com.example.entity.InsurancePolicy;
import com.example.entity.Vehicle;
import com.example.entity.VehicleTrips;
import com.example.entity.VehicleViolation;
import com.example.repository.InsurancePolicyRepository;
import com.example.repository.VehicleRepository;
import com.example.repository.VehicleTripsRepository;
import com.example.repository.VehicleViolationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComplianceService {

    private static final String EXPIRED_INSURANCE = "EXPIRED_INSURANCE";
    private static final String UNINSURED_TRIP = "UNINSURED_TRIP";

    private final VehicleRepository vehicleRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final VehicleTripsRepository vehicleTripsRepository;
    private final VehicleViolationRepository vehicleViolationRepository;

    public ComplianceService(
            VehicleRepository vehicleRepository,
            InsurancePolicyRepository insurancePolicyRepository,
            VehicleTripsRepository vehicleTripsRepository,
            VehicleViolationRepository vehicleViolationRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.vehicleTripsRepository = vehicleTripsRepository;
        this.vehicleViolationRepository = vehicleViolationRepository;
    }

    @Transactional
    public List<VehicleViolation> runComplianceCheck() {
        List<VehicleViolation> newViolations = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Vehicle vehicle : vehicleRepository.findAll()) {
            Integer vehicleId = vehicle.getVehicleId();
            if (vehicleId == null) {
                continue;
            }

            List<InsurancePolicy> policies = insurancePolicyRepository.findPoliciesForVehicle(vehicleId);
            List<VehicleTrips> trips = vehicleTripsRepository.findTripHistoryByVehicleId(vehicleId);

            createExpiredInsuranceViolation(vehicle, policies, today).ifPresent(violation -> {
                if (saveViolationIfMissing(violation)) {
                    newViolations.add(violation);
                }
            });

            for (VehicleTrips trip : trips) {
                if (!isTripCoveredByAnyPolicy(trip, policies)) {
                    VehicleViolation violation = buildUninsuredTripViolation(vehicle, trip);
                    if (saveViolationIfMissing(violation)) {
                        newViolations.add(violation);
                    }
                }
            }
        }

        return newViolations;
    }

    private Optional<VehicleViolation> createExpiredInsuranceViolation(
            Vehicle vehicle,
            List<InsurancePolicy> policies,
            LocalDate today
    ) {
        if (policies.isEmpty()) {
            return Optional.empty();
        }

        InsurancePolicy latestPolicy = policies.get(0);
        LocalDate expiryDate = latestPolicy.getExpiryDate();
        if (expiryDate == null || !expiryDate.isBefore(today)) {
            return Optional.empty();
        }

        VehicleViolation violation = new VehicleViolation();
        violation.setVehicleId(vehicle.getVehicleId());
        violation.setViolationType(EXPIRED_INSURANCE);
        violation.setDateTime(expiryDate.atStartOfDay());
        violation.setDescription("Latest insurance policy expired on " + expiryDate + ".");
        return Optional.of(violation);
    }

    private VehicleViolation buildUninsuredTripViolation(Vehicle vehicle, VehicleTrips trip) {
        VehicleViolation violation = new VehicleViolation();
        violation.setVehicleId(vehicle.getVehicleId());
        violation.setViolationType(UNINSURED_TRIP);
        violation.setDateTime(trip.getDateTime());
        violation.setDescription(
                "Trip " + trip.getAction()
                        + " at " + trip.getLocation()
                        + " was recorded without an active insurance policy."
        );
        return violation;
    }

    private boolean isTripCoveredByAnyPolicy(VehicleTrips trip, List<InsurancePolicy> policies) {
        LocalDateTime tripDateTime = trip.getDateTime();
        if (tripDateTime == null) {
            return false;
        }

        for (InsurancePolicy policy : policies) {
            LocalDate issueDate = policy.getIssueDate();
            LocalDate expiryDate = policy.getExpiryDate();
            if (issueDate == null || expiryDate == null) {
                continue;
            }

            boolean startsAfterOrOnIssueDate = !tripDateTime.toLocalDate().isBefore(issueDate);
            boolean endsBeforeOrOnExpiryDate = !tripDateTime.toLocalDate().isAfter(expiryDate);
            if (startsAfterOrOnIssueDate && endsBeforeOrOnExpiryDate) {
                return true;
            }
        }

        return false;
    }

    private boolean saveViolationIfMissing(VehicleViolation violation) {
        boolean exists = vehicleViolationRepository.existsByVehicleIdAndViolationTypeAndDateTime(
                violation.getVehicleId(),
                violation.getViolationType(),
                violation.getDateTime()
        );
        if (exists) {
            return false;
        }

        vehicleViolationRepository.save(violation);
        return true;
    }
}
