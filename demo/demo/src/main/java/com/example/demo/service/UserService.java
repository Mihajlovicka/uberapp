package com.example.demo.service;

import com.example.demo.email.EmailDetails;
import com.example.demo.email.EmailService;
import com.example.demo.exception.ApiRequestException;
import com.example.demo.model.Address;
import com.example.demo.model.ClientsAccount;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repository.ClientsRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    ClientsRepository clientsRepository;

    @Autowired private EmailService emailService;

    public ClientsAccount saveClient(ClientsAccount clientsAccount) throws ApiRequestException {
        if(userRepository.findUserByEmail(clientsAccount.getUser().getEmail()) != null) {
            throw new ApiRequestException("Email in use.");
        }
        final Address address = addressService.save(clientsAccount.getAddress());
        clientsAccount.setAddress(address);

        clientsAccount.getUser().setStatus(Status.NOTACTIVATED);
        final User user = userRepository.save(clientsAccount.getUser());
        clientsAccount.setUser(user);
        //sanje meila
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Potrvda registracije UberApp");
        emailDetails.setRecipient("saramihajlovic94@gmail.com");
        emailDetails.setMsgBody("Kliknite na link kako bi se registrovali. \n " +
                "<a href=\"localhost:4200/registerConfirm/"+emailDetails.getRecipient()+"\">Potvrda registracije</a>");
        emailService.send(emailDetails);

        return clientsRepository.save(clientsAccount);
    }

    public void registerConfirm(String email) throws ApiRequestException {
        User u = userRepository.findUserByEmail(email);
        if(u == null) {
            throw new ApiRequestException("Email not found.");
        }
        u.setStatus(Status.ACTIVE);
        userRepository.save(u);
    }
}
