package com.example.demo.fakeBank;

public class AddBankAccountDTO {
    private Double balance;
    private String accountNumber;
    private String email;

    private String name;

    private String surname;

    public AddBankAccountDTO(){}
    public AddBankAccountDTO(Double balance, String accountNumber, String email, String name, String surname){
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
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
}

