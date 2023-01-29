package com.example.demo.controller;

import com.example.demo.dto.FavoriteRideDTO;
import com.example.demo.dto.RideSimulationDTO;
import com.example.demo.model.Car;
import com.example.demo.model.DriverStatus;
import com.example.demo.model.FavoriteRide;
import com.example.demo.model.RideSimulation;
import com.example.demo.service.CarService;
import com.example.demo.service.RideService;
import com.example.demo.service.RideSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/ride")
public class RideSimController {

    @Autowired
    private RideSimulationService rideSimService;
    @Autowired
    private CarService carService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<RideSimulationDTO> createRide(@RequestBody RideSimulationDTO rideDTO) {
        RideSimulation ride = this.rideSimService.createRide(new RideSimulation(rideDTO), new Car(rideDTO.getVehicle()));
        DriverStatus status = carService.getCarStatus(ride.getCar());
        RideSimulationDTO returnRideDTO = new RideSimulationDTO(ride, status);
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            produces = "application/json"
    )
    public ResponseEntity<RideSimulationDTO> changeRide(@PathVariable("id") int id) {
        RideSimulation ride = this.rideSimService.changeRide(id);
        DriverStatus status = carService.getCarStatus(ride.getCar());
        RideSimulationDTO returnRideDTO = new RideSimulationDTO(ride, status);
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @GetMapping(
            path = "/{id}",
            produces = "application/json"
    )
    public ResponseEntity<RideSimulationDTO> getRealRide(@PathVariable("id") int id) {
        RideSimulation ride = this.rideSimService.getRealRide(id);
        DriverStatus status = carService.getCarStatus(ride.getCar());
        RideSimulationDTO rideDTO = new RideSimulationDTO(ride, status);
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-existing-ride", rideDTO);
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
        //obavestiti zbog fronta
    }

    @DeleteMapping(
            path = "/{id}",
            produces = "text/plain"
    )
    public ResponseEntity<Map> deleteRide(@PathVariable("id") int  id) {
        int car_id = this.rideSimService.deleteRide(id);
        Map map = new HashMap<String, Integer>();
        map.put("carId", car_id);
        map.put("rideId", id);
        this.simpMessagingTemplate.convertAndSend("/map-updates/delete-ride", map);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
