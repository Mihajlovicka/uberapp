package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table
public class DriversAccountChange{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long user_changing_id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String carBrand;

    @Column(nullable = false)
    private String carModel;

    @Column(nullable = false)
    private String carColor;

    @Column(nullable = false)
    private String carPlateNumber;

    @Column(nullable = true)
    private CarBodyType carBodyType;

    @Column(nullable = true)
    private Fuel carFuelType;



    @Column
    private String picture;

    @Column
    private String phone;

    @Column
    private DriverStatus driverStatus;



    public DriversAccountChange(){}


    public DriversAccountChange(Long id, Long user_changing_id, String name, String surname, String email, String carBrand, String carModel, String carColor, String carPlateNumber, CarBodyType carBodyType, Fuel carFuelType, String picture, String phone, DriverStatus driverStatus) {
        this.id = id;
        this.user_changing_id = user_changing_id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carColor = carColor;
        this.carPlateNumber = carPlateNumber;
        this.carBodyType = carBodyType;
        this.carFuelType = carFuelType;
        this.picture = picture;
        this.phone = phone;
        this.driverStatus = driverStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_changing_id() {
        return user_changing_id;
    }

    public void setUser_changing_id(Long user_changing_id) {
        this.user_changing_id = user_changing_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    public CarBodyType getCarBodyType() {
        return carBodyType;
    }

    public void setCarBodyType(CarBodyType carBodyType) {
        this.carBodyType = carBodyType;
    }

    public Fuel getCarFuelType() {
        return carFuelType;
    }

    public void setCarFuelType(Fuel carFuelType) {
        this.carFuelType = carFuelType;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }
}
