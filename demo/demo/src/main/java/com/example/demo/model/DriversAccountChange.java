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



    @Column
    private String picture;

    @Column
    private String phone;



    public DriversAccountChange(){}


    public DriversAccountChange(Long id, Long user_changing_id, String name, String surname, String email, String picture, String phone) {
        this.id = id;
        this.user_changing_id = user_changing_id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.picture = picture;
        this.phone = phone;
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
}
