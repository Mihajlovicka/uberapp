package com.example.demo.dto;

import com.example.demo.model.DriverStatus;

public class DriverAccountDTO {
    private UserDTO user;

    private ImageDTO picture;

    private String phone;

    private CarDTO car;

    private DriverStatus driverStatus;

    private boolean driversAvailability;


    public DriverAccountDTO(){}

    public DriverAccountDTO(UserDTO user, ImageDTO picture, String phone, CarDTO car, DriverStatus driverStatus, boolean driversAvailability){
        this.user = user;
        this.picture = picture;
        this.phone = phone;
        this.car = car;
        this.driverStatus = driverStatus;
        this.driversAvailability = driversAvailability;
    }

    public boolean getDriversAvailability() {
        return driversAvailability;
    }

    public void setDriversAvailability(boolean driversAvailability) {
        this.driversAvailability = driversAvailability;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ImageDTO getPicture() {
        return picture;
    }

    public void setPicture(ImageDTO picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }
}
