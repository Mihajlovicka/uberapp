package com.example.demo.fakeBank;

import com.example.demo.email.EmailDetails;
import com.example.demo.email.EmailService;
import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.TransactionIdDoesNotExistException;
import com.example.demo.model.*;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class BankService {
    @Autowired
    private BankRepository bankRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    BankTransactionRepository bankTransactionRepository;




    public ClientsBankAccount create(ClientsBankAccount clientsBankAccount){
        return bankRepository.save(clientsBankAccount);
    }



    public ClientsBankAccount findByAccountNumber(String accountNumber){
        return bankRepository.getClientsBankAccountByAccountNumber(accountNumber);
    }

    public BankTransaction getById(Long id) throws TransactionIdDoesNotExistException {
        BankTransaction bankTransaction =  bankTransactionRepository.findById(id).get();
        if(bankTransaction == null) throw new TransactionIdDoesNotExistException("Transaction with this id does not exist");
        return bankTransaction;
    }



    public ClientsBankAccount reserveMoney(Double amount, ClientsBankAccount clientsBankAccount){
        clientsBankAccount.setBalance(clientsBankAccount.getBalance()-amount);
        return bankRepository.save(clientsBankAccount);

    }

    public ClientsBankAccount closeReservation(Double amount, ClientsBankAccount clientsBankAccount){
        clientsBankAccount.setBalance(clientsBankAccount.getBalance()+amount);
        return bankRepository.save(clientsBankAccount);
    }

    public BankTransaction requestOwnerPayment(Drive drive){
        BankTransaction transaction = new BankTransaction();

        transaction.setAmount(drive.getOwnerDebit());
        transaction.setTransactionStatus(TransactionStatus.WAITING_VERIFICATION);
        transaction.setSender(drive.getOwner().getClientsBankAccount().getVerificationEmail());
        transaction.setReceiver("UBER");
        transaction.setTransactionType(TransactionType.OUTFLOW);

        reserveMoney(drive.getOwnerDebit(), drive.getOwner().getClientsBankAccount());

        BankTransaction saved =  bankTransactionRepository.save(transaction);

        requestPaymentEmail(drive.getOwner(), saved.getId());

        return saved;

    }

    public Double findPassengersDebit(Drive drive, String passengersEmail){
        Double amount = 0.0;
        for (Passenger passenger:
             drive.getPassengers()) {
            if(passenger.getPassengerEmail().equals(passengersEmail)) amount = passenger.getDebit();
        }

        return amount;
    }

    //kreiranje transakcije za passengera
    public BankTransaction requestPassengerPayment(Drive drive, ClientsAccount passengerClientsAccount){
        BankTransaction transaction = new BankTransaction();

        transaction.setAmount(findPassengersDebit(drive, passengerClientsAccount.getUser().getEmail()));
        transaction.setTransactionStatus(TransactionStatus.WAITING_VERIFICATION);
        transaction.setSender(passengerClientsAccount.getUser().getEmail());
        transaction.setReceiver("UBER");
        transaction.setTransactionType(TransactionType.OUTFLOW);

        reserveMoney(transaction.getAmount(), passengerClientsAccount.getClientsBankAccount());

        BankTransaction saved =  bankTransactionRepository.save(transaction);

        requestPaymentEmail(passengerClientsAccount, saved.getId());

        return saved;
    }

    //od ownera trazi
    public void requestPaymentEmail(ClientsAccount clientsAccount, Long transactionId){
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Placanje");
        emailDetails.setRecipient(clientsAccount.getClientsBankAccount().getVerificationEmail());
        emailDetails.setMsgBody("UberApp zahteva pristup Vasem nalogu kako bi se izvrsilo placanje. \n " +
                "http://localhost:4200/passenger/confirmPayment/"+transactionId);
        emailService.send(emailDetails);
    }





    public void sendVerificationEmail(ClientsAccount clientsAccount){

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Pristup nalogu");
        emailDetails.setRecipient("jelenamanojlovic27062000@gmail.com");
        emailDetails.setMsgBody("Kliknite na link kako bi potvrdili/odbili zahtev za pristup. \n " +
                "http://localhost:4200/bankConfirm/"+clientsAccount.getUser().getEmail());
        emailService.send(emailDetails);

    }

    public BankTransaction acceptTransaction(Long id) throws TransactionIdDoesNotExistException {
        BankTransaction bankTransaction = getById(id);
        bankTransaction.setTransactionStatus(TransactionStatus.WAITING_FINALIZATION);
        return bankTransactionRepository.save(bankTransaction);
    }

    public BankTransaction declineTransaction(Long id, String clientsBankAccountNumber) throws TransactionIdDoesNotExistException {
        BankTransaction bankTransaction = getById(id);
        closeReservation(bankTransaction.getAmount(), findByAccountNumber(clientsBankAccountNumber));

        bankTransaction.setTransactionStatus(TransactionStatus.FAILED);

        return bankTransactionRepository.save(bankTransaction);
    }






}
