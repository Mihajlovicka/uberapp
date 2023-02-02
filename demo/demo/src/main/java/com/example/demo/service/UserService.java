package com.example.demo.service;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.UsersChatDisplayDTO;
import com.example.demo.email.EmailDetails;
import com.example.demo.email.EmailService;
import com.example.demo.exception.*;
import com.example.demo.fakeBank.BankService;
import com.example.demo.fakeBank.ClientsBankAccount;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserConverter userConverter;

    public ClientsAccount saveClient(ClientsAccount clientsAccount) throws EmailExistException {

        if(userRepository.findUserByEmail(clientsAccount.getUser().getEmail()) != null) {
            throw new EmailExistException("Email in use.");
        }
        clientsAccount.getUser().setPassword(passwordEncoder.encode(clientsAccount.getUser().getPassword()));
        final Address address = addressService.save(clientsAccount.getAddress());
        clientsAccount.setAddress(address);

        clientsAccount.getUser().setUsername(clientsAccount.getUser().getEmail());
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

        driversAccount.getUser().setUsername(driversAccount.getUser().getEmail());
        driversAccount.getUser().setStatus(Status.ACTIVE);
        driversAccount.getUser().setPassword(passwordEncoder.encode(driversAccount.getUser().getPassword()));
        driversAccount.getUser().setEnabled(true);
        final User user = userRepository.save(driversAccount.getUser());

        driversAccount.setUser(user);
        driversAccount.setDriverStatus(DriverStatus.AVAILABLE);

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
            throw new EmailNotFoundException(String.format("No user found with email '%s'.", email));
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
        oldClient.getUser().setUsername(newClient.getUser().getUsername());
        oldClient.getUser().setName(newClient.getUser().getName());
        oldClient.getUser().setSurname(newClient.getUser().getSurname());
        oldClient.setPhone(newClient.getPhone());
        oldClient.getAddress().setCity(newClient.getAddress().getCity());
        oldClient.getAddress().setStreet(newClient.getAddress().getStreet());
        oldClient.getAddress().setNumber(newClient.getAddress().getNumber());
        clientsRepository.save(oldClient);
        return oldClient;
    }

    public ClientsAccount changeBankStatus(String email, BankStatus bankStatus){
        ClientsAccount client = getClientByEmail(email);

        if(bankStatus.equals(BankStatus.EMPTY)){
            client.setClientsBankAccount(null);
        }


        client.setBankStatus(bankStatus);
        return clientsRepository.save(client);
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

        driversAccountChange.setUsername(newDriver.getUser().getUsername());
        driversAccountChange.setName(newDriver.getUser().getName());
        driversAccountChange.setSurname(newDriver.getUser().getSurname());
        driversAccountChange.setPhone(newDriver.getPhone());
        //driversAccountChange.setPicture(newDriver.getPicture());
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

    public List<ClientsAccount> getAllActiveClients(){

        List<ClientsAccount> all = clientsRepository.findAllByUserStatus(Status.ACTIVE);

        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().equals(User.class)){// RADI NE DIRAJTE POBOGU!!!
            User logged = (User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            if(logged.getRole().getName().equals("ROLE_CLIENT")){
                for (ClientsAccount client:
                     all) {
                    if(client.getUser().getEmail().equals(logged.getEmail())){
                        all.remove(client);
                        return all;
                    }
                }
            }
        }

        return all;

    }


    public List<DriversAccount> getDrivers(){
        List<DriversAccount> drivers =  driversRepository.findAll();
        return drivers;
    }
    public List<ClientsAccount> getClients(){
        return clientsRepository.findAll();
    }

    public void changeDriverStatus(DriversAccount da,DriverStatus status) {
        da.setDriverStatus(status);
        driversRepository.save(da);
    }
    public Image savePictureInUser(Image image, String email) {
        User logged = userRepository.findUserByEmail(email);
        Long oldID = 0L;
        Image img = null;
        if(logged!= null) {
            if (logged.getRole().getName().equals("ROLE_CLIENT")) {
                for (ClientsAccount client : clientsRepository.findAll()) {
                    if (client.getUser().getEmail().equals(logged.getEmail())) {
                        if (client.getPicture() != null) {
                            oldID = client.getPicture().getId();
                            image.setId(oldID);
                        }
                        image = imageRepository.save(image);
                        client.setPicture(image);
                        clientsRepository.save(client);
                        img = image;
                    }
                }
            } else if (logged.getRole().getName().equals("ROLE_DRIVER")) {
                for (DriversAccount driver : driversRepository.findAll()) {
                    if (driver.getUser().getEmail().equals(logged.getEmail())) {
                        if (driver.getPicture() != null) {
                            oldID = driver.getPicture().getId();
                            image.setId(oldID);
                        }
                        image = imageRepository.save(image);
                        driver.setPicture(image);
                        driversRepository.save(driver);
                        img = image;
                    }
                }
            }
        }
        return img;
    }



    public User getLoggedUser(){
        User logged = null;
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().equals(User.class)){// RADI NE DIRAJTE POBOGU!!!
            logged = (User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        return logged;
    }



    public User getLoggedIn(){
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().equals(User.class)){// JELENA OVO NE RADIIIIIIIII!!!
            return  (User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
        throw new NotFoundException("User not found or not logged in.");
    }


    public void block(String email) {
        for(User user : userRepository.findAll()){
            if(user.getEmail().equals(email)){
                user.setStatus(Status.BANNED);
                userRepository.save(user);
                break;
            }
        }

    }


    public void unblock(String email) {
        for(User user : userRepository.findAll()){
            if(user.getEmail().equals(email)){
                user.setStatus(Status.ACTIVE);
                userRepository.save(user);
                break;
            }
        }

    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findUserByEmail(email);
        if(user != null){
            String s = passwordEncoder.encode(oldPassword);
            if(passwordEncoder.matches(oldPassword, user.getPassword())){
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public List<DriversAccount> getDriversByStatus(DriverStatus status){
        return this.driversRepository.getByDriverStatus(status);
    }

    public DriversAccount getDriver(Long id) {
        DriversAccount driver = driversRepository.findById(id).orElseThrow(() -> new NotFoundException("Vozac ne postoji."));
        return driver;
    }

    public User getAdmin() {
        for(User user : getAdmins()){
            if(user.getRole().getName().equals("ROLE_ADMINISTRATOR")){
                return user;
            }
        }
        return null;
    }

    public List<User> getAdmins() {
        List<User> admins = new ArrayList<User>();
        for(User user : userRepository.findAll()){
            if(user.getRole().getName().equals("ROLE_ADMINISTRATOR")){
                admins.add(user);
            }
        }
        return admins;
    }

    public DriversAccount getLoggedDriver(){
        User user = getLoggedIn();
        DriversAccount driver = getDriver(user.getEmail());
        return driver;
    }

    public void updateDriverStatus(DriverStatus status) {
        DriversAccount driver = getLoggedDriver();
        driver.setDriverStatus(status);
        driversRepository.save(driver);
    }


    public User findByEmail(String email){return userRepository.findUserByEmail(email);}

}
