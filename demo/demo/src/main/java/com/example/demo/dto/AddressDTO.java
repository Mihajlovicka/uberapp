package com.example.demo.dto;

public class AddressDTO {
    private String city;
    private String street;
    private String number;

    public AddressDTO(){}

    public AddressDTO(String city, String street, String number){
        this.city = city;
        this.street = street;
        this.number = number;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
