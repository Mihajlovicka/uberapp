package com.example.demo.dto;

public class RegisterFormDTO {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private AddressDTO address;
    private String password;
    private String checkPassword;

    public RegisterFormDTO(){}

    public RegisterFormDTO(String name, String surname, String email, String phone, AddressDTO addressDTO, String password, String checkPassword){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = addressDTO;
        this.password = password;
        this.checkPassword = checkPassword;
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
}
