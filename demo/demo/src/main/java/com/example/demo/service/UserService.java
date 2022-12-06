package com.example.demo.service;

import com.example.demo.email.EmailDetails;
import com.example.demo.email.EmailService;
import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.PlateNumberExistException;
import com.example.demo.model.*;
import com.example.demo.repository.ClientsRepository;
import com.example.demo.repository.DriversRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    ClientsRepository clientsRepository;

    @Autowired
    CarService carService;

    @Autowired
    DriversRepository driversRepository;

    @Autowired private EmailService emailService;

    public ClientsAccount saveClient(ClientsAccount clientsAccount) throws EmailExistException {
        if(userRepository.findUserByEmail(clientsAccount.getUser().getEmail()) != null) {
            throw new EmailExistException("Email in use.");
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

    public DriversAccount saveDriver(DriversAccount driversAccount) throws EmailExistException, PlateNumberExistException {
        if(userRepository.findUserByEmail(driversAccount.getUser().getEmail()) != null){
            throw new EmailExistException("Email in use.");
        }

        final Car car = carService.save(driversAccount.getCar());
        driversAccount.setCar(car);

        driversAccount.getUser().setStatus(Status.ACTIVE);
        final User user = userRepository.save(driversAccount.getUser());

        driversAccount.setUser(user);

        return driversRepository.save(driversAccount);
    }

    public void registerConfirm(String email) throws EmailExistException {
        User u = userRepository.findUserByEmail(email);
        if(u == null) {
            throw new EmailExistException("Email not found.");
        }
        u.setStatus(Status.ACTIVE);
        userRepository.save(u);
    }
}
