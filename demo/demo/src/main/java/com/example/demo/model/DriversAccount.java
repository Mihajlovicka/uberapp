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

    @Column
    private String picture;

    @Column
    private String phone;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    public DriversAccount(){}

    public DriversAccount(Long id, User user, String picture, String phone, Car car) {
        this.id = id;
        this.user = user;
        this.picture = picture;
        this.phone = phone;
        this.car = car;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
