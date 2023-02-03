package com.example.demo.fakeBank;


public class ClientsBankAccountDTO {
    private Double balance;
    private String accountNumber;

    private String verificationEmail;

    private String ownerName;

    private String ownerSurname;

    public ClientsBankAccountDTO(){
    }

    public ClientsBankAccountDTO(Double balance, String accountNumber, String verificationEmail, String ownerName, String ownerSurname){
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.verificationEmail = verificationEmail;
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
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

