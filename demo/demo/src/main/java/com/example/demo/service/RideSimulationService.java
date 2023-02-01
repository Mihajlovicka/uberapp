package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.*;
import com.example.demo.model.help.ResponseRouteHelp;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.RideSimulationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RideSimulationService {

    @Autowired
    private RideSimulationRepository rideRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    @Lazy
    private DriveService driveService;

    public RideSimulation createRide(RideSimulation ride, Car vehicle) {
        RideSimulation returnRide = rideRepository.save(ride);
        Car storedVehicle = this.carRepository.findById(vehicle.getId()).orElseThrow(
                () -> new NotFoundException("Vehicle does not exist"));
        returnRide.setCar(storedVehicle);
        return this.rideRepository.save(returnRide);
    }

    public RideSimulation changeRide(int id) {
        RideSimulation ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        ride.setRideStatus(RideStatus.ENDED);
        return this.rideRepository.save(ride);
    }

    public List<RideSimulation> getRides(RideStatus status) {
        return this.rideRepository.findAllByRideStatus(status);
    }

    public RideSimulation getRealRide(int id) {
        for (RideSimulation r : getRides(RideStatus.STARTED)) {
            if (r.getCar().getId() == id) return r;
        }
        Drive drive = driveService.getCarCurrentDrive(id);
        if (drive != null) {
            return createRideSim(drive);
        }
        throw new NotFoundException("No current drive for this car");
    }

    public int deleteRide(int id) {
        RideSimulation ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        int car_id = Math.toIntExact(ride.getCar().getId());
        rideRepository.deleteById(id);
        return car_id;
    }

    public RideSimulation createRideSim(Drive d) {
        RideSimulation ride = new RideSimulation();
        ride.setRideStatus(RideStatus.STARTED);
        ride.setCar(d.getDriver().getCar());
        ride.setRouteJSON(d.getRouteJSON());
        return rideRepository.save(ride);
    }

}
