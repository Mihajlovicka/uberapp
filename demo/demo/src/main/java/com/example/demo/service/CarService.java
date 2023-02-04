package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.PlateNumberExistException;
import com.example.demo.model.*;
import com.example.demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarService {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired@Lazy private DriveService driveService;


    //izmeniti jedan vozac jedna kola i to je to!!!!!!!
    public Car save(Car car) throws EmailExistException, PlateNumberExistException {
        if (carRepository.findCarByPlateNumber(car.getPlateNumber()) != null)
            throw new PlateNumberExistException("Registration plate number exist.");
        return carRepository.save(car);
    }


    private List<Car> getAllActiveCars() {
        ArrayList<Car> cars = new ArrayList<>();
        userService.getDrivers().forEach((u) -> {
            cars.add(u.getCar());
        });
        return cars;
    }

    private Car changeCarOnMap(Car car) {
        car.setCarOnMap(!car.isCarOnMap());
        return carRepository.save(car);
    }

    public Car getCarNotOnMap() {
        for (Car c : getAllActiveCars()) {
            if (!c.isCarOnMap()) {
                return changeCarOnMap(c);
            }
        }
        return null;
    }

    public Car updateCarLocation(int id, Double latitude, Double longitude) {
        Car vehicle = this.carRepository.findById((long) id).orElseThrow(() -> new NotFoundException("Vehicle does not exist!"));
        vehicle.setCurrentLocation(new Location(latitude, longitude));
        return carRepository.save(vehicle);
    }

    public DriverStatus getCarStatus(Car car) {
        for (DriversAccount da : userService.getDrivers()) {
            if (da.getCar().getId() == car.getId()) return da.getDriverStatus();
        }
        throw new NotFoundException("Status for car not found!");
    }

    public String getCarStatus(int id) {
        for (DriversAccount da : userService.getDrivers()) {
            if (da.getCar().getId() == id)
                return da.getDriverStatus().name();
        }
        throw new NotFoundException("Status for car not found!");
    }

    public void changeStatus(Car car, DriverStatus status) {
        for (DriversAccount da : userService.getDrivers()) {
            if (da.getCar().getId() == car.getId()) {
                userService.changeDriverStatus(da, status);
                return;
            }
        }
        throw new NotFoundException("Status for car not found!");
    }

    public Car getDriversCar() {
        User user = userService.getLoggedIn();
        DriversAccount driver = userService.getDriver(user.getEmail());
        return driver.getCar();
    }

    public Car getCar(Long id) {
        Car c = carRepository.findById(id).orElseThrow(() -> new NotFoundException("Car not found."));
        return c;
    }

    public Car getClientCurrentCar() {
        return driveService.getClientCurrentDrive().getDriver().getCar();
    }

    public List<Car> getAllCars() {
        return this.carRepository.findAll();
    }
}
