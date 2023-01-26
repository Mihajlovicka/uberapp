package com.example.demo.dto;

import com.example.demo.model.Car;
import com.example.demo.model.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarSimulationDTO {
    private int id;
    private String licensePlateNumber;
    private double latitude;
    private double longitude;
    private String status;

    public CarSimulationDTO(Car vehicle, DriverStatus status) {
        this.id = Math.toIntExact(vehicle.getId());
        this.licensePlateNumber = vehicle.getPlateNumber();
        this.latitude = vehicle.getCurrentLocation().getLatitude();
        this.longitude = vehicle.getCurrentLocation().getLongitude();
        this.status = status.toString();
    }
}
