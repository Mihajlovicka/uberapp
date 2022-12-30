package com.example.demo.fakeBank;


public class ClientsBankAccountDTO {
    private Long balance;
    private String accountNumber;

    public ClientsBankAccountDTO(){
    }

    public ClientsBankAccountDTO(Long balance, String accountNumber){
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getBalance() {
        return balance;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

