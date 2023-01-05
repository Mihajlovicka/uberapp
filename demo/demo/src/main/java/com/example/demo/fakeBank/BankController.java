package com.example.demo.fakeBank;

import com.example.demo.dto.ClientAccountDTO;
import com.example.demo.exception.BankAccountNumberDoNotExistException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.model.ClientsAccount;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {

    @Autowired
    BankService bankService;

    @Autowired
    UserService userService;

    @Autowired
    BankConverter bankConverter;

    // ne poziva se sa fronta..samo da se napuni baza
    @PostMapping(value = "bank/create")
    public ResponseEntity create(@RequestBody AddBankAccountDTO addBankAccountDTO) throws BankAccountNumberDoNotExistException {
        ClientsBankAccount clientsBankAccount = new ClientsBankAccount();
        clientsBankAccount.setBalance(addBankAccountDTO.getBalance());
        ClientsBankAccount acc = bankService.findByAccountNumber(addBankAccountDTO.getAccountNumber());
        if(acc==null){
        clientsBankAccount.setAccountNumber(addBankAccountDTO.getAccountNumber());}
        else{
            clientsBankAccount = acc;
        }

        clientsBankAccount.setVerificationEmail(addBankAccountDTO.getEmail());
        clientsBankAccount.setOwnerName(addBankAccountDTO.getName());
        clientsBankAccount.setOwnerSurname(addBankAccountDTO.getSurname());



        final ClientsBankAccount saved = bankService.create(clientsBankAccount);

        return new ResponseEntity(bankConverter.toDto(saved), HttpStatus.CREATED);
    }



    }





