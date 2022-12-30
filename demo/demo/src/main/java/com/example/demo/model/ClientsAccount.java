package com.example.demo.model;

import com.example.demo.fakeBank.ClientsBankAccount;

import javax.persistence.*;

@Entity
@Table
public class ClientsAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column
    private String picture;

    @Column
    private String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clients_bank_account_id", nullable = true)
    private ClientsBankAccount clientsBankAccount;

    @Column(nullable = false)
    private BankStatus bankStatus;

    public ClientsAccount(){}

    public ClientsAccount(Long id, User user, Address address, String picture, String phone, ClientsBankAccount clientsBankAccount, BankStatus bankStatus){
        this.id = id;
        this.user = user;
        this.address = address;
        this.picture = picture;
        this.phone = phone;
        this.clientsBankAccount = clientsBankAccount;
        this.bankStatus = bankStatus;
    }

    public Long getId(){return id;}

    public void setId(Long id){this.id = id;}

    public User getUser(){return user;}

    public void setUser(User user){this.user = user;}

    public Address getAddress(){return address;}

    public void setAddress(Address address){this.address = address;}

    public String getPicture(){return picture;}

    public void setPicture(String picture) {this.picture = picture;}

    public String getPhone(){return phone;}

    public void setPhone(String phone){ this.phone = phone;}

    public ClientsBankAccount getClientsBankAccount() {
        return clientsBankAccount;
    }

    public void setClientsBankAccount(ClientsBankAccount clientsBankAccount) {
        this.clientsBankAccount = clientsBankAccount;
    }

    public BankStatus getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(BankStatus bankStatus) {
        this.bankStatus = bankStatus;
    }
}
