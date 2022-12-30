package com.example.demo.fakeBank;

import com.example.demo.email.EmailDetails;
import com.example.demo.email.EmailService;
import com.example.demo.model.ClientsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankService {
    @Autowired
    private BankRepository bankRepository;

    @Autowired
    EmailService emailService;

    public ClientsBankAccount create(ClientsBankAccount clientsBankAccount){
        return bankRepository.save(clientsBankAccount);
    }

    //dodati exc da ne postoji racun
    //+ dodavanje - placanje
    public ClientsBankAccount makeTransaction(String accountNumber, Long money){
        ClientsBankAccount clientsBankAccount = bankRepository.getClientsBankAccountByAccountNumber(accountNumber);
        clientsBankAccount.setBalance(clientsBankAccount.getBalance() + money);

        return clientsBankAccount;
    }

    public ClientsBankAccount findByAccountNumber(String accountNumber){
        return bankRepository.getClientsBankAccountByAccountNumber(accountNumber);
    }

    public void sendVerificationEmail(ClientsAccount clientsAccount){
        System.out.println("uso sam");
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Pristup nalogu");
        emailDetails.setRecipient("jelenamanojlovic27062000@gmail.com");
        emailDetails.setMsgBody("Kliknite na link kako bi potvrdili/odbili zahtev za pristup. \n " +
                "<a href=\"localhost:4200/bankConfirm/"+emailDetails.getRecipient()+"\">Potvrda pristupanju nalogu</a>");
        emailService.send(emailDetails);

    }

}