package com.example.demo.dto;

import com.example.demo.model.Role;
import com.example.demo.model.Status;

public class UserDTO {
    private String name;
    private String surname;
    private String email;


    private Status status;

    private Role role;


    public UserDTO(){}

    public UserDTO(String name, String surname, String email, Status status, Role role){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.status = status;
        this.role = role;
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


    public Status getStatus(){return status;}

    public void setStatus(Status status){this.status = status;}

    public Role getRole(){return role;}

    public void setRole(Role role){this.role = role;}
}

