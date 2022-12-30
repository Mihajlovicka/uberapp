package com.example.demo.dto;

import com.example.demo.model.DriverStatus;

public class DriverAccountDTO {
    private UserDTO user;

    private String picture;

    private String phone;

    private CarDTO car;

    private DriverStatus driverStatus;

    public DriverAccountDTO(){}

    public DriverAccountDTO(UserDTO user, String picture, String phone, CarDTO car, DriverStatus driverStatus){
        this.user = user;
        this.picture = picture;
        this.phone = phone;
        this.car = car;
        this.driverStatus = driverStatus;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
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
