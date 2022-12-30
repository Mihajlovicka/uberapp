package com.example.demo.fakeBank;

public class AddBankAccountDTO {
    private Long balance;
    private String accountNumber;
    private String email;

    public AddBankAccountDTO(){}
    public AddBankAccountDTO(Long balance, String accountNumber, String email){
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.email = email;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getBalance() {
        return balance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

