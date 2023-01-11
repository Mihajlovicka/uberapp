package com.example.demo.converter;

import com.example.demo.dto.CarDTO;
import com.example.demo.model.Car;
import org.springframework.stereotype.Component;

@Component
public class CarConverter {
    public CarDTO todto(Car car){
        return new CarDTO(car.getBrand(), car.getModel(), car.getColor(), car.getPlateNumber(), car.getBodyType(), car.getFuelType(), car.getNumOfSeats());
    }
}
