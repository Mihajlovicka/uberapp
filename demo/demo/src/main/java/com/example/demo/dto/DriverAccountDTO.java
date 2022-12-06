package com.example.demo.dto;

public class DriverAccountDTO {
    private UserDTO userDTO;

    private String picture;

    private String phone;

    private CarDTO carDTO;

    public DriverAccountDTO(){}

    public DriverAccountDTO(UserDTO userDTO, String picture, String phone, CarDTO carDTO){
        this.userDTO = userDTO;
        this.picture = picture;
        this.phone = phone;
        this.carDTO = carDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
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

    public CarDTO getCarDTO() {
        return carDTO;
    }

    public void setCarDTO(CarDTO carDTO) {
        this.carDTO = carDTO;
    }
}
