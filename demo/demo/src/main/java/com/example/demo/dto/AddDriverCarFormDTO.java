package com.example.demo.dto;

import com.example.demo.model.Role;

public class AddDriverCarFormDTO {
    private String name;
    private String surname;
    private String email;

    private String phone;
    private String password;
    private String checkPassword;
    private Role role;
    private CarDTO car;

    public AddDriverCarFormDTO(){}

    public AddDriverCarFormDTO(String name, String surname, String email, String phone, String password, String checkPassword, Role role, CarDTO car){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.checkPassword = checkPassword;
        this.role = role;
        this.car = car;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }
}
