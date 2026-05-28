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

    private static final String UNINSURED_TRIP = "UNINSURED_TRIP";
    private static final String TRIP_START = "TRIP_START";
    private static final String TRIP_END = "TRIP_END";

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

            createIllegalUsageViolation(vehicle, policies, trips, today).ifPresent(violation -> {
                if (saveViolationIfMissing(violation)) {
                    newViolations.add(violation);
                }
            });
        }

        return newViolations;
    }

    private Optional<VehicleViolation> createIllegalUsageViolation(
            Vehicle vehicle,
            List<InsurancePolicy> policies,
            List<VehicleTrips> trips,
            LocalDate today
    ) {
        if (policies.isEmpty()) {
            return Optional.empty();
        }

        InsurancePolicy latestPolicy = policies.get(0);
        LocalDate expiryDate = latestPolicy.getExpiryDate();
        if (expiryDate == null || expiryDate.isAfter(today)) {
            return Optional.empty();
        }

        Optional<VehicleTrips> lastTripStart = findLastMovementByAction(trips, TRIP_START);
        Optional<VehicleTrips> lastTripEnd = findLastMovementByAction(trips, TRIP_END);

        if (lastTripStart.isEmpty()) {
            return Optional.empty();
        }

        VehicleTrips start = lastTripStart.get();
        LocalDateTime startDateTime = start.getDateTime();
        if (!startDateTime.toLocalDate().isAfter(expiryDate)) {
            return Optional.empty();
        }

        if (lastTripEnd.isPresent() && !startDateTime.isAfter(lastTripEnd.get().getDateTime())) {
            return Optional.empty();
        }

        return Optional.of(buildUninsuredTripViolation(vehicle, start, expiryDate));
    }

    private Optional<VehicleTrips> findLastMovementByAction(List<VehicleTrips> trips, String action) {
        VehicleTrips lastMovement = null;
        for (VehicleTrips trip : trips) {
            if (trip.getDateTime() != null && action.equalsIgnoreCase(trip.getAction())) {
                lastMovement = trip;
            }
        }
        return Optional.ofNullable(lastMovement);
    }

    private VehicleViolation buildUninsuredTripViolation(
            Vehicle vehicle,
            VehicleTrips tripStart,
            LocalDate expiryDate
    ) {
        VehicleViolation violation = new VehicleViolation();
        violation.setVehicleId(vehicle.getVehicleId());
        violation.setViolationType(UNINSURED_TRIP);
        violation.setDateTime(tripStart.getDateTime());
        violation.setDescription(
                "Vehicle started a trip at "
                        + tripStart.getLocation()
                        + " after the latest insurance policy expired on "
                        + expiryDate
                        + "."
        );
        return violation;
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
