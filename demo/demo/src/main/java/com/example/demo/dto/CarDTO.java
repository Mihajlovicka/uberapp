package com.example.demo.dto;

import com.example.demo.model.CarBodyType;
import com.example.demo.model.Fuel;

public class CarDTO {
    private String brand;

    private String model;

    private String color;

    private String plateNumber;

    private CarBodyType bodyType;

    private Fuel fuelType;

    public CarDTO(String brand, String model, String color, String plateNumber, CarBodyType bodyType, Fuel fuelType){
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.plateNumber = plateNumber;
        this.bodyType = bodyType;
        this.fuelType = fuelType;
    }

    public CarDTO(){}

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public CarBodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(CarBodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Fuel getFuelType() {
        return fuelType;
    }

    public void setFuelType(Fuel fuelType) {
        this.fuelType = fuelType;
    }
}
