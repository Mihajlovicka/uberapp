package com.example.demo.fakeBank;


import javax.persistence.*;

@Table
@Entity
public class ClientsBankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long balance;

    // prvih 3-->banka, 13-->broj racuna, kontrolni broj-->2
    //ukupno 18 cifara
    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = true)
    private String verificationEmail;

    @Column(nullable = true)
    private String ownerName;

    @Column(nullable = true)
    private String ownerSurname;

    public ClientsBankAccount(){}

    public ClientsBankAccount(Long id,Long balance, String accountNumber, String verificationEmail, String ownerName, String ownerSurname){
        this.id = id;
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.verificationEmail = verificationEmail;
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getVerificationEmail() {
        return verificationEmail;
    }

    public void setVerificationEmail(String verificationEmail) {
        this.verificationEmail = verificationEmail;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerSurname() {
        return ownerSurname;
    }

    public void setOwnerSurname(String ownerSurname) {
        this.ownerSurname = ownerSurname;
    }
}
