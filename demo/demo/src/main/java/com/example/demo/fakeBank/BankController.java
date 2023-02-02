package com.example.demo.fakeBank;

import com.example.demo.dto.ClientAccountDTO;
import com.example.demo.exception.BankAccountNumberDoNotExistException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.TransactionIdDoesNotExistException;
import com.example.demo.model.ClientsAccount;
import com.example.demo.service.DriveService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BankController {

    @Autowired
    BankService bankService;

    @Autowired
    UserService userService;

    @Autowired
    BankConverter bankConverter;

    @Autowired
    NotificationService notificationService;

    @Autowired
    DriveService driveService;


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


    //vrati transakciju
    @GetMapping(value="passenger/confirmPayment/{id}")
    public ResponseEntity getTransaction(@PathVariable Long id) throws TransactionIdDoesNotExistException {
        return new ResponseEntity(bankConverter.toDTO(bankService.getById(id)), HttpStatus.OK);
    }

    @PostMapping(value="bank/acceptTransaction/{id}")
    public ResponseEntity acceptTransaction(@PathVariable Long id) throws TransactionIdDoesNotExistException {
        return new ResponseEntity(bankConverter.toDTO(bankService.acceptTransaction(id)), HttpStatus.OK);
    }

    @PostMapping(value="bank/declineTransaction/{id}")
    public ResponseEntity declineTransaction(@PathVariable Long id, @RequestBody ClientsBankAccountDTO clientsBankAccount) throws TransactionIdDoesNotExistException {
        BankTransaction bankTransaction = bankService.declineTransaction(id, clientsBankAccount.getAccountNumber());
        //postaviti status voznje na failed
        driveService.driveFailedMoneyTransactionRejected(bankTransaction);

        notificationService.paymentFailedDriveCanceledNotify(bankTransaction.getSender());
        return new ResponseEntity(bankConverter.toDTO(bankTransaction), HttpStatus.OK);
    }

    }





