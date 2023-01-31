package com.example.demo.dto;

import com.example.demo.model.*;

import java.util.List;
import java.util.Set;

public class DriveDTO {

    private List<RealAddress> stops;

    private Double distance;

    private Double duration;

    private Double price;

    private Set<Passenger> passengers;

    private int seats;

    private int baby;

    private int babySeats;

    private int pets;

    private DriveStatus driveStatus;

    private ClientAccountDTO owner;

    private String routeJSON;

    private DriverAccountDTO driver;

    private boolean splitBill;

    private String date;

    public DriveDTO(){

    }

    public DriveDTO(List<RealAddress> stops, Double distance, Double duration, Double price, Set<Passenger> passengers, int seats, int baby, int babySeats, int pets, DriveStatus driveStatus, ClientAccountDTO owner, String routeJSON, DriverAccountDTO driver, boolean splitBill, String Date) {
        this.stops = stops;
        this.distance = distance;
        this.duration = duration;
        this.price = price;
        this.passengers = passengers;
        this.seats = seats;
        this.baby = baby;
        this.babySeats = babySeats;
        this.pets = pets;
        this.driveStatus = driveStatus;
        this.owner = owner;
        this.routeJSON = routeJSON;
        this.driver = driver;
        this.splitBill = splitBill;
        this.date = date;
    }

    public List<RealAddress> getStops() {
        return stops;
    }

    public void setStops(List<RealAddress> stops) {
        this.stops = stops;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getBaby() {
        return baby;
    }

    public void setBaby(int baby) {
        this.baby = baby;
    }

    public int getBabySeats() {
        return babySeats;
    }

    public void setBabySeats(int babySeats) {
        this.babySeats = babySeats;
    }

    public int getPets() {
        return pets;
    }

    public void setPets(int pets) {
        this.pets = pets;
    }

    public DriveStatus getDriveStatus() {
        return driveStatus;
    }

    public void setDriveStatus(DriveStatus driveStatus) {
        this.driveStatus = driveStatus;
    }

    public ClientAccountDTO getOwner() {
        return owner;
    }

    public void setOwner(ClientAccountDTO owner) {
        this.owner = owner;
    }

    public String getRouteJSON() {
        return routeJSON;
    }

    public void setRouteJSON(String routeJSON) {
        this.routeJSON = routeJSON;
    }

    public DriverAccountDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverAccountDTO driver) {
        this.driver = driver;
    }

    public boolean isSplitBill() {
        return splitBill;
    }

    public void setSplitBill(boolean splitBill) {
        this.splitBill = splitBill;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}