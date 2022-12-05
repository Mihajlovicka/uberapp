package com.example.demo.dto;

public class ClientAccountDTO {
   private UserDTO userDTO;

   private AddressDTO addressDTO;

   private String picture;

   private String phone;

   public ClientAccountDTO(UserDTO userDTO, AddressDTO addressDTO, String picture, String phone){
       this.userDTO = userDTO;
       this.addressDTO = addressDTO;
       this.picture = picture;
       this.phone = phone;
   }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
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
