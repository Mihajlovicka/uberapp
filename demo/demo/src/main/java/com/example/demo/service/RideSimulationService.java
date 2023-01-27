package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Car;
import com.example.demo.model.DriverStatus;
import com.example.demo.model.RideSimulation;
import com.example.demo.model.RideStatus;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.RideSimulationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<RideSimulation> getRides() {
        return this.rideRepository.findAllByRideStatus(RideStatus.STARTED);
    }

    public RideSimulation getRealRide(int id) {
        for(RideSimulation r: getRides()){
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
}
