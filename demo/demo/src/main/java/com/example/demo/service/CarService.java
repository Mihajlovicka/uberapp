package com.example.demo.service;

import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.PlateNumberExistException;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarService {

    @Autowired
    CarRepository carRepository;


    //izmeniti jedan vozac jedna kola i to je to!!!!!!!
    public Car save(Car car) throws EmailExistException, PlateNumberExistException {
        if(carRepository.findCarByPlateNumber(car.getPlateNumber()) != null) throw new PlateNumberExistException("Registration plate number exist.");
        return carRepository.save(car);
    }
}
