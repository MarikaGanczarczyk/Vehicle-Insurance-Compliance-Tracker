package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_trips")
public class VehicleTrips {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int vehicleId;

    private LocalDateTime dateTime;

    private String action; // TRIP_START or TRIP_END

    private String location;

    // Default constructor
    public VehicleTrips() {
    }

    // Constructor
    public VehicleTrips(int vehicleId, LocalDateTime dateTime, String action, String location) {
        this.vehicleId = vehicleId;
        this.dateTime = dateTime;
        this.action = action;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
