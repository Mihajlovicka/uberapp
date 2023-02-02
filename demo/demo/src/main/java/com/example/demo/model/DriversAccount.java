package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table
public class DriversAccount{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "picture_id")
    private Image picture;

    @Column
    private String phone;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column
    private DriverStatus driverStatus;

    @Column
    private boolean driversAvailability = false;

    public DriversAccount(){}

    public DriversAccount(Long id, User user, Image picture, String phone, Car car, DriverStatus driverStatus, boolean driversAvailability) {
        this.id = id;
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

    public Long getId(){return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }
}
