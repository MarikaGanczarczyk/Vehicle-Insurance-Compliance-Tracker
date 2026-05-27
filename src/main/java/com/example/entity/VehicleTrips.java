package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_trips")
public class VehicleTrips {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rowId;

    private int vehicleId;

    private LocalDateTime dateTime;

    private String tripActions; // TRIP_START or TRIP_END

    private String locations;


    public int getId() {
        return rowId;
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
        return tripActions;
    }

    public void setAction(String action) {
        this.tripActions = action;
    }

    public String getLocation() {
        return locations;
    }

    public void setLocation(String location) {
        this.locations = location;
    }
}
