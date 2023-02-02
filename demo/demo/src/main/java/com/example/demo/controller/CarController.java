package com.example.demo.controller;

import com.example.demo.dto.CarDTO;
import com.example.demo.dto.CarSimulationDTO;
import com.example.demo.model.Car;
import com.example.demo.model.DriverStatus;
import com.example.demo.model.Location;
import com.example.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "api/car")
public class CarController {

    @Autowired
    private CarService carService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping(
            produces = "application/json"
    )
    public ResponseEntity<CarSimulationDTO> getCar() {
        Car car = carService.getCarNotOnMap();
        DriverStatus status = carService.getCarStatus(car);
        CarSimulationDTO returnCarDTO = new CarSimulationDTO(car, status);
        return new ResponseEntity<>(returnCarDTO, HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<CarSimulationDTO> updateCarLocation(@PathVariable("id") int id, @RequestBody Location locationDTO) {
        Car car = this.carService.updateCarLocation(id, locationDTO.getLatitude(), locationDTO.getLongitude());
        DriverStatus status = carService.getCarStatus(car);
        CarSimulationDTO returnCarDTO = new CarSimulationDTO(car, status);
        this.simpMessagingTemplate.convertAndSend("/map-updates/update-car-position", returnCarDTO);
        return new ResponseEntity<>(returnCarDTO, HttpStatus.OK);
    }

    @PostMapping(
            path = "/timeLeft/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<String> updateTimeLeft(@PathVariable("id") int id, @RequestBody Map<String, String> time) {
        time.put("id", String.valueOf(id));
        this.simpMessagingTemplate.convertAndSend("/map-updates/update-time-left", time);
        return new ResponseEntity("", HttpStatus.OK);
    }

    @GetMapping(
            path = "/status/{id}",
            produces = "application/json"
    )
    public ResponseEntity<String> getCarStatus(@PathVariable("id") int id) {
        String s = carService.getCarStatus(id);
        Map<String, String> map = new HashMap<>();
        map.put("status", s);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @GetMapping(
            path = "/getDriversCar",
            produces = "application/json"
    )
    public ResponseEntity<CarSimulationDTO> getDriversCar() {
        Car car = carService.getDriversCar();
        DriverStatus status = carService.getCarStatus(car);
        return new ResponseEntity<>(new CarSimulationDTO(car,status), HttpStatus.OK);
    }

    @GetMapping(
            path = "/test/{text}",
            produces = "application/json"
    )
    public void test(@PathVariable("text") String text) {
       System.out.println(text);
    }


    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping(
            path = "/getClientCurrentCar",
            produces = "application/json"
    )
    public ResponseEntity<CarSimulationDTO> getClientCurrentCar() {
        Car car = carService.getClientCurrentCar();
        DriverStatus status = carService.getCarStatus(car);
        return new ResponseEntity<>(new CarSimulationDTO(car,status), HttpStatus.OK);
    }

}
