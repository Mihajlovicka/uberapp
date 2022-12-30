package com.example.demo.service;

import com.example.demo.email.EmailDetails;
import com.example.demo.email.EmailService;
import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.PlateNumberExistException;
import com.example.demo.exception.BankAccountNumberDoNotExistException;
import com.example.demo.fakeBank.BankService;
import com.example.demo.fakeBank.ClientsBankAccount;
import com.example.demo.model.*;
import com.example.demo.repository.ClientsRepository;
import com.example.demo.repository.DriversChangeRepository;
import com.example.demo.repository.DriversRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BankService bankService;

    @Autowired
    AddressService addressService;

    @Autowired
    ClientsRepository clientsRepository;

    @Autowired
    DriversChangeRepository driversChangeRepository;

    @Autowired
    CarService carService;

    @Autowired
    DriversRepository driversRepository;

    @Autowired
    private EmailService emailService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClientsAccount saveClient(ClientsAccount clientsAccount) throws EmailExistException {

        if(userRepository.findUserByEmail(clientsAccount.getUser().getEmail()) != null) {
            throw new EmailExistException("Email in use.");
        }
        clientsAccount.getUser().setPassword(passwordEncoder.encode(clientsAccount.getUser().getPassword()));
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
                "http://localhost:4200/registerConfirm/"+emailDetails.getRecipient());
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
        driversAccount.getUser().setPassword(passwordEncoder.encode(driversAccount.getUser().getPassword()));
        driversAccount.getUser().setEnabled(true);
        final User user = userRepository.save(driversAccount.getUser());

        driversAccount.setUser(user);

        return driversRepository.save(driversAccount);
    }

    public void registerConfirm(String email) throws EmailExistException {
        User u = userRepository.findUserByEmail(email);
        if(u == null) {
            throw new EmailExistException("Email not found.");
        }
        u.setEnabled(true);
        u.setStatus(Status.ACTIVE);
        userRepository.save(u);
    }

    public User getByEmail(String email) throws EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }

    }

    public ClientsAccount findClientsAccount(String email) throws EmailNotFoundException {
        ClientsAccount clientsAccount = clientsRepository.findClientsAccountByUserEmail(email);
        if (clientsAccount == null) {
            throw new EmailNotFoundException(String.format("No client found with email '%s'.", email));
        } else {
            return clientsAccount;
        }
    }

    public DriversAccount findDriversAccount(String email) throws EmailNotFoundException {
        DriversAccount driversAccount = driversRepository.findDriversAccountByUserEmail(email);
        if (driversAccount == null) {
            throw new EmailNotFoundException(String.format("No driver found with email '%s'.", email));
        } else {
            return driversAccount;
        }
    }


    public UserDetails loadUserByEmail(String email) throws EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(String.format("No user found with username '%s'.", email));
        } else {
            return user;
        }
    }


    public ClientsAccount connectBankAccount(String bankAccNumber, ClientsAccount clientsAccount) throws BankAccountNumberDoNotExistException {
        ClientsBankAccount clientsBankAccount = bankService.findByAccountNumber(bankAccNumber);
        if(clientsBankAccount == null){
            throw new BankAccountNumberDoNotExistException("Account number does not exist.");

        }
        clientsAccount.setClientsBankAccount(clientsBankAccount);
        clientsAccount.setBankStatus(BankStatus.NOTCONFIRMED);
        //slanje emaila
        bankService.sendVerificationEmail(clientsAccount);
        return clientsAccount;
    }


    private ClientsAccount getClientByEmail(String email){
        ClientsAccount client = null;
        for(ClientsAccount c : clientsRepository.findAll()){
            if(c.getUser().getEmail().equals(email)){
                client = c;
                break;
            }
        }
        return client;
    }

    private DriversAccount getDriverByEmail(String email){
        DriversAccount driver = null;
        for(DriversAccount d : driversRepository.findAll()){
            if(d.getUser().getEmail().equals(email)){
                driver = d;
                break;
            }
        }
        return driver;
    }

    public ClientsAccount getClient(String email) throws EmailNotFoundException {
        ClientsAccount client = getClientByEmail(email);
        if (client == null) {
            throw new EmailNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return client;
        }
    }

    public ClientsAccount updateClient(ClientsAccount newClient){
        ClientsAccount oldClient = getClientByEmail(newClient.getUser().getEmail());
        oldClient.getUser().setName(newClient.getUser().getName());
        oldClient.getUser().setSurname(newClient.getUser().getSurname());
        oldClient.setPhone(newClient.getPhone());
        oldClient.getAddress().setCity(newClient.getAddress().getCity());
        oldClient.getAddress().setStreet(newClient.getAddress().getStreet());
        oldClient.getAddress().setNumber(newClient.getAddress().getNumber());
        clientsRepository.save(oldClient);
        return oldClient;
    }

    public DriversAccount getDriver(String email) {
        DriversAccount driver = getDriverByEmail(email);
        if (driver == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", email));
        } else {
            return driver;
        }
    }

    private DriversAccountChange checkChanging(String email){
        for(DriversAccountChange dac : driversChangeRepository.findAll()){
            if(dac.getEmail().equals(email)) return dac;
        }
        return new DriversAccountChange();
    }

    public DriversAccount updateDriver(DriversAccount newDriver) {
        DriversAccount oldDriver = getDriverByEmail(newDriver.getUser().getEmail());
        DriversAccountChange driversAccountChange = checkChanging(newDriver.getUser().getEmail());

        driversAccountChange.setName(newDriver.getUser().getName());
        driversAccountChange.setSurname(newDriver.getUser().getSurname());
        driversAccountChange.setPhone(newDriver.getPhone());
        driversAccountChange.setPicture(newDriver.getPicture());
        driversAccountChange.setCarBrand(newDriver.getCar().getBrand());
        driversAccountChange.setCarBodyType(newDriver.getCar().getBodyType());
        driversAccountChange.setCarModel(newDriver.getCar().getModel());
        driversAccountChange.setCarFuelType(newDriver.getCar().getFuelType());
        driversAccountChange.setCarColor(newDriver.getCar().getColor());
        driversAccountChange.setCarPlateNumber(newDriver.getCar().getPlateNumber());
        driversAccountChange.setUser_changing_id(oldDriver.getId());
        driversAccountChange.setEmail(newDriver.getUser().getEmail());
        driversAccountChange.setDriverStatus(newDriver.getDriverStatus());

        //status postojeceg na underrevision
        oldDriver.getUser().setStatus(Status.UNDERREVISION);
        userRepository.save(oldDriver.getUser());

        driversChangeRepository.save(driversAccountChange);
        return oldDriver;
    }
}
