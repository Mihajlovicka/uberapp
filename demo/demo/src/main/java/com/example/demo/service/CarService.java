package com.example.demo.service;

import com.example.demo.exception.EmailExistException;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarService {

    @Autowired
    CarRepository carRepository;


    //izmeniit jedan vozac jedna kola i to je to!!!!!!!
    public Car save(Car car) throws EmailExistException {
        if(carRepository.findCarByPlateNumber(car.getPlateNumber()) != null) throw new EmailExistException("Registration plate number exist.");
        return carRepository.save(car);
    }
}
