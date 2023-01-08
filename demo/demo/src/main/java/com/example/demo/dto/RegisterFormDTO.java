package com.example.demo.dto;

import com.example.demo.model.Role;

public class RegisterFormDTO {

    private String username;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private AddressDTO address;
    private String password;
    private String checkPassword;

    private String role;

    private String bankAccountNumber;

    public RegisterFormDTO(){}

    public RegisterFormDTO(String username, String name, String surname, String email, String phone, AddressDTO address, String password, String checkPassword, String role, String bankAccountNumber){
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.checkPassword = checkPassword;
        this.role = role;
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
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

    public String getRole(){return role;}

    public void setRole(String role){this.role = role;}

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}
