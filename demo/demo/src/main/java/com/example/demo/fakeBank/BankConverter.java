package com.example.demo.fakeBank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankConverter {
    @Autowired
    BankService bankService;

    public ClientsBankAccountDTO toDto(ClientsBankAccount clientsBankAccount){
        return new ClientsBankAccountDTO(clientsBankAccount.getBalance(), clientsBankAccount.getAccountNumber(), clientsBankAccount.getVerificationEmail(), clientsBankAccount.getOwnerName(), clientsBankAccount.getOwnerSurname());
    }

    public ClientsBankAccount fromDto(ClientsBankAccountDTO clientsBankAccountDTO){
        ClientsBankAccount clientsBankAccount = bankService.findByAccountNumber(clientsBankAccountDTO.getAccountNumber());
        return clientsBankAccount;
    }
}
