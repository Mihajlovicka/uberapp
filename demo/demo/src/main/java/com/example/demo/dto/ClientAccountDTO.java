package com.example.demo.dto;

public class ClientAccountDTO {
   private UserDTO user;

   private AddressDTO address;

   private String picture;

   private String phone;

    public ClientAccountDTO() {
    }

    public ClientAccountDTO(UserDTO userDTO, AddressDTO addressDTO, String picture, String phone){
       this.user = userDTO;
       this.address = addressDTO;
       this.picture = picture;
       this.phone = phone;
   }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
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
