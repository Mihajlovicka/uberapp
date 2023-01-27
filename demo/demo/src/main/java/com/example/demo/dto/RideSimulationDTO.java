package com.example.demo.dto;

import com.example.demo.model.DriverStatus;
import com.example.demo.model.RideSimulation;
import com.example.demo.model.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideSimulationDTO {
    private int id;
    private String routeJSON;
    private RideStatus rideStatus;
    private CarSimulationDTO vehicle;

    public RideSimulationDTO(RideSimulation ride, DriverStatus status) {
        this.id = ride.getId();
        this.routeJSON = ride.getRouteJSON();
        this.rideStatus = ride.getRideStatus();
        this.vehicle = new CarSimulationDTO(ride.getCar(), status);
    }
}
