package com.example.demo.dto;

import com.example.demo.model.Passenger;
import com.example.demo.model.RealAddress;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Set;

public class CreateDriveReservationDTO {
    private List<RealAddress> stops;

    private Double distance;

    private Double duration;

    private Double price;

    private Set<Passenger> passengers;

    private int seats;

    private int baby;

    private int babySeats;

    private int pets;

    private ClientAccountDTO owner;

    //@Type(type = "json")
    private String routeJSON;

    private boolean splitBill;

    private String date;

    private Double ownerDebit;

    public CreateDriveReservationDTO(){}

    public CreateDriveReservationDTO(List<RealAddress> stops, Double distance, Double duration, Double price, Set<Passenger> passengers, int seats, int baby, int babySeats, int pets, ClientAccountDTO owner, String routeJSON, boolean splitBill, String date, Double ownerDebit) {
        this.stops = stops;
        this.distance = distance;
        this.duration = duration;
        this.price = price;
        this.passengers = passengers;
        this.seats = seats;
        this.baby = baby;
        this.babySeats = babySeats;
        this.pets = pets;
        this.owner = owner;
        this.routeJSON = routeJSON;
        this.splitBill = splitBill;
        this.date = date;
        this.ownerDebit = ownerDebit;
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

    public Double getOwnerDebit() {
        return ownerDebit;
    }

    public void setOwnerDebit(Double ownerDebit) {
        this.ownerDebit = ownerDebit;
    }
}
