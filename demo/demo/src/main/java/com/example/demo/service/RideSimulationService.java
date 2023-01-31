package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.*;
import com.example.demo.model.help.ResponseRouteHelp;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.RideSimulationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CarService carService;

    public RideSimulation createRide(RideSimulation ride, Car vehicle) {
        RideSimulation returnRide =rideRepository.save(ride);
        Car storedVehicle = this.carRepository.findById(vehicle.getId()).orElseThrow(
                () -> new NotFoundException("Vehicle does not exist"));
        returnRide.setCar(storedVehicle);
        return this.rideRepository.save(returnRide);
    }

    public RideSimulation changeRide(int id) {
        RideSimulation ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        ride.setRideStatus(RideStatus.ENDED);
        //???????????????????//////

        carService.changeStatus(ride.getCar(), DriverStatus.AVAILABLE);

        //?????????????????????????
        return this.rideRepository.save(ride);
    }

    public List<RideSimulation> getRides(RideStatus status) {
        return this.rideRepository.findAllByRideStatus(status);
    }

    public RideSimulation getRealRide(int id) {
        for(RideSimulation r: getRides(RideStatus.STARTED)){
            if(r.getCar().getId() == id) return r;
        }
        throw new NotFoundException("Ride does not exist");
    }

    public int deleteRide(int id) {
        RideSimulation ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        int car_id = Math.toIntExact(ride.getCar().getId());
        rideRepository.deleteById(id);
        return car_id;
    }

    public Location getCarEndStop(Long id) throws JsonProcessingException {
        Car c = carRepository.findById(id).orElseThrow(() -> new NotFoundException("Car not found."));
        RideSimulation ride = rideRepository.findByCar(c);
        if(ride == null) {
            throw new NotFoundException("Ride not found.");
        }
        ResponseRouteHelp route = new ObjectMapper().readValue(ride.getRouteJSON(), ResponseRouteHelp.class);
        ArrayList<ArrayList<Double>> coords = route.getMetadata().getQuery().getCoordinates();
        return new Location(coords.get(1).get(1),coords.get(1).get(0));
    }
}
