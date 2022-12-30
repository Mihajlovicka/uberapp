package com.example.demo.fakeBank;

import org.springframework.stereotype.Component;

@Component
public class BankConverter {
    public ClientsBankAccountDTO toDto(ClientsBankAccount clientsBankAccount){
        return new ClientsBankAccountDTO(clientsBankAccount.getBalance(), clientsBankAccount.getAccountNumber());
    }
}
