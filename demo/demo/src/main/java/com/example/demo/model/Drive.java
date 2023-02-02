package com.example.demo.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table
@TypeDef(name = "json", typeClass = JsonType.class)
public class Drive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RealAddress> stops;

    @Column
    private Double distance;

    @Column
    private Double duration;

    @Column
    private Double price;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Passenger> passengers;

    @Column
    private int seats;

    @Column
    private int baby;

    @Column
    private int babySeats;

    @Column
    private int pets;

    @Column
    private DriveStatus driveStatus;

    @Column
    private DriveType driveType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private ClientsAccount owner;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String routeJSON;

    @ManyToOne(fetch = FetchType.EAGER)
    private DriversAccount driver;

    @Column
    private boolean splitBill;


    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @Column
    private Date date;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @Column
    private Date startDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @Column
    private Date endDate;

    @Column
    private Double ownerDebit;

    public Drive(){}

    public Drive(Long id, List<RealAddress> stops, Double distance, Double duration, Double price, Set<Passenger> passengers, int seats, int baby, int babySeats, int pets, ClientsAccount owner, String routeJSON, DriversAccount driver, DriveStatus driveStatus, boolean splitBill, Date date, DriveType driveTimeStatus, Double ownerDebit, Date startDate, Date endDate) {
        this.id = id;
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
        this.driver = driver;
        this.driveStatus = driveStatus;
        this.splitBill = splitBill;
        this.date = date;
        this.ownerDebit = ownerDebit;
        this.driveType = driveTimeStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public DriveType getDriveType() {
        return driveType;
    }

    public void setDriveType(DriveType driveTimeStatus) {
        this.driveType = driveTimeStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRouteJSON() {
        return routeJSON;
    }

    public void setRouteJSON(String routeJSON) {
        this.routeJSON = routeJSON;
    }

    public ClientsAccount getOwner() {
        return owner;
    }

    public void setOwner(ClientsAccount owner) {
        this.owner = owner;
    }

    public List<RealAddress> getStops() {
        return stops;
    }

    public void setStops(List<RealAddress> stops) {
        this.stops = stops;
    }

    public DriversAccount getDriver() {
        return driver;
    }

    public void setDriver(DriversAccount driver) {
        this.driver = driver;
    }

    public DriveStatus getDriveStatus() {
        return driveStatus;
    }

    public void setDriveStatus(DriveStatus driveStatus) {
        this.driveStatus = driveStatus;
    }

    public void setSplitBill(boolean splitBill) {
        this.splitBill = splitBill;
    }

    //SETTER :D
    public boolean isSplitBill() {
        return splitBill;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getOwnerDebit() {
        return ownerDebit;
    }

    public void setOwnerDebit(Double ownerDebit) {
        this.ownerDebit = ownerDebit;
    }
}
