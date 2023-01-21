package com.example.demo.dto;

import com.example.demo.fakeBank.ClientsBankAccountDTO;
import com.example.demo.model.BankStatus;

public class ClientAccountDTO {
   private UserDTO user;

   private AddressDTO address;

   private ImageDTO picture;

   private String phone;

    private ClientsBankAccountDTO clientsBankAccount;

    private BankStatus bankStatus;

    public ClientAccountDTO(){}


   public ClientAccountDTO(UserDTO user, AddressDTO address, ImageDTO picture, String phone, ClientsBankAccountDTO clientsBankAccount, BankStatus bankStatus){
       this.user = user;
       this.address = address;
       this.picture = picture;
       this.phone = phone;
       this.clientsBankAccount = clientsBankAccount;
       this.bankStatus = bankStatus;
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

    public ImageDTO getPicture() {
        return picture;
    }

    public void setPicture(ImageDTO picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ClientsBankAccountDTO getClientsBankAccount() {
        return clientsBankAccount;
    }

    public void setClientsBankAccount(ClientsBankAccountDTO clientsBankAccount) {
        this.clientsBankAccount = clientsBankAccount;
    }

    public BankStatus getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(BankStatus bankStatus) {
        this.bankStatus = bankStatus;
    }
}
