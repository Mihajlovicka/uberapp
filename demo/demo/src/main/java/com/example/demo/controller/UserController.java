package com.example.demo.controller;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.AddDriverCarFormDTO;
import com.example.demo.dto.ClientAccountDTO;
import com.example.demo.dto.DriverAccountDTO;
import com.example.demo.dto.RegisterFormDTO;
import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.PlateNumberExistException;
import com.example.demo.exception.BankAccountNumberDoNotExistException;
import com.example.demo.fakeBank.BankConverter;
import com.example.demo.fakeBank.BankService;
import com.example.demo.model.*;
import com.example.demo.model.Address;
import com.example.demo.model.ClientsAccount;
import com.example.demo.model.DriversAccount;
import com.example.demo.model.User;
import com.example.demo.service.ImageService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    BankService bankService;

    @Autowired
    UserConverter userConverter;

    @Autowired
    RoleService roleService;

    @Autowired
    ImageService imageService;

    @Autowired
    BankConverter bankConverter;

    @PostMapping(value="api/register")
    public ResponseEntity register(@RequestBody RegisterFormDTO registerFormDTO) throws EmailExistException, BankAccountNumberDoNotExistException {
        User user = new User();
        Address address = new Address();
        ClientsAccount clientsAccount = new ClientsAccount();


        //user.setUsername(registerFormDTO.getUsername());
        user.setName(registerFormDTO.getName());
        user.setSurname(registerFormDTO.getSurname());
        user.setEmail(registerFormDTO.getEmail());
        user.setPassword(registerFormDTO.getPassword());
        user.setRole(roleService.findByName(registerFormDTO.getRole()));

        address.setCity(registerFormDTO.getAddress().getCity());
        address.setStreet(registerFormDTO.getAddress().getStreet());
        address.setNumber(registerFormDTO.getAddress().getNumber());


        clientsAccount.setUser(user);
        clientsAccount.setAddress(address);
        clientsAccount.setPicture(null);
        clientsAccount.setPhone(registerFormDTO.getPhone());

        if(registerFormDTO.getBankAccountNumber() != ""){
            clientsAccount = userService.connectBankAccount(registerFormDTO.getBankAccountNumber(), clientsAccount);

        }

        else{
            clientsAccount.setBankStatus(BankStatus.EMPTY);
        }

        final ClientsAccount savedAccount = userService.saveClient(clientsAccount);
        return new ResponseEntity(userConverter.toDTO(savedAccount), HttpStatus.OK);


    }

    @GetMapping(value = "/api/getClient")
    public ResponseEntity getClient(@RequestParam String email) {
        System.out.println(email);

        try {
            ClientsAccount ca = userService.getClient(email);
            if(ca.getPicture()==null) ca.setPicture(new Image());
            else ca.getPicture().setPicByte(ImageService.decompressBytes(ca.getPicture().getPicByte()));
            ClientAccountDTO clientAccountDTO = userConverter.toDTO(ca);
            return new ResponseEntity(clientAccountDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("", HttpStatus.BAD_REQUEST);
        } catch (EmailNotFoundException e) {
            return new ResponseEntity("", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/getDriver")
    public ResponseEntity getDriver(@RequestParam String email) {

        try {
            DriversAccount da = userService.getDriver(email);
            if(da.getPicture()==null) da.setPicture(new Image());
            else da.getPicture().setPicByte(ImageService.decompressBytes(da.getPicture().getPicByte()));
            DriverAccountDTO driverAccountDTO = userConverter.toDTO(da);
            return new ResponseEntity(driverAccountDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="api/updateClient")
    public ResponseEntity updateClient(@RequestBody ClientAccountDTO clientAccountDTO) {
        //ClientsAccount newClient = userConverter.fromDTO(clientAccountDTO);
        ClientsAccount clientsAccount = new ClientsAccount();
        clientsAccount.setUser(new User());
        clientsAccount.setAddress(new Address());
        clientsAccount.getUser().setUsername(clientAccountDTO.getUser().getEmail());
        clientsAccount.getUser().setName(clientAccountDTO.getUser().getName());
        clientsAccount.getUser().setSurname(clientAccountDTO.getUser().getSurname());
        clientsAccount.getUser().setEmail(clientAccountDTO.getUser().getEmail());
        clientsAccount.getUser().setRole(clientAccountDTO.getUser().getRole());
        clientsAccount.getUser().setStatus(clientAccountDTO.getUser().getStatus());

        clientsAccount.getAddress().setCity(clientAccountDTO.getAddress().getCity());
        clientsAccount.getAddress().setStreet(clientAccountDTO.getAddress().getStreet());
        clientsAccount.getAddress().setNumber(clientAccountDTO.getAddress().getNumber());

        clientsAccount.setPhone(clientAccountDTO.getPhone());
        //clientsAccount.setPicture(clientAccountDTO.getPicture());

        clientsAccount.setBankStatus(clientAccountDTO.getBankStatus());
        clientsAccount.setClientsBankAccount(bankConverter.fromDto(clientAccountDTO.getClientsBankAccount()));

        return new ResponseEntity(userConverter.toDTO(userService.updateClient(clientsAccount)), HttpStatus.OK);

    }

    @PostMapping(value="api/updateDriver")
    public ResponseEntity updateDriver(@RequestBody DriverAccountDTO driverAccountDTO) {
        //DriversAccount newDriver = userConverter.fromDTO(driverAccountDTO);
        DriversAccount driversAccount = new DriversAccount();
        driversAccount.setUser(new User());
        driversAccount.setCar(new Car());
        driversAccount.getUser().setUsername(driverAccountDTO.getUser().getEmail());
        driversAccount.getUser().setName(driverAccountDTO.getUser().getName());
        driversAccount.getUser().setSurname(driverAccountDTO.getUser().getSurname());
        driversAccount.getUser().setEmail(driverAccountDTO.getUser().getEmail());
        driversAccount.getUser().setRole(driverAccountDTO.getUser().getRole());
        driversAccount.getUser().setStatus(driverAccountDTO.getUser().getStatus());

        driversAccount.getCar().setBodyType(driverAccountDTO.getCar().getBodyType());
        driversAccount.getCar().setBrand(driverAccountDTO.getCar().getBrand());
        driversAccount.getCar().setColor(driverAccountDTO.getCar().getColor());
        driversAccount.getCar().setModel(driverAccountDTO.getCar().getModel());
        driversAccount.getCar().setFuelType(driverAccountDTO.getCar().getFuelType());
        driversAccount.getCar().setPlateNumber(driverAccountDTO.getCar().getPlateNumber());
        driversAccount.getCar().setNumOfSeats(driverAccountDTO.getCar().getNumOfSeats());
        driversAccount.setDriverStatus(driverAccountDTO.getDriverStatus());

        driversAccount.setPhone(driverAccountDTO.getPhone());
        //driversAccount.setPicture(driverAccountDTO.getPicture());

        return new ResponseEntity(userConverter.toDTO(userService.updateDriver(driversAccount)), HttpStatus.OK);

    }


        @PostMapping(value = "api/add-driver")
    public ResponseEntity addDriver(@RequestBody AddDriverCarFormDTO addDriverCarFormDTO) throws EmailExistException, PlateNumberExistException {
        User user = new User();
        Car car = new Car();
        DriversAccount driverAccount = new DriversAccount();

        //user.setUsername(addDriverCarFormDTO.getUsername());
        user.setName(addDriverCarFormDTO.getName());
        user.setSurname(addDriverCarFormDTO.getSurname());
        user.setEmail(addDriverCarFormDTO.getEmail());
        user.setPassword(addDriverCarFormDTO.getPassword());
            user.setRole(roleService.findByName(addDriverCarFormDTO.getRole()));


        car.setBrand(addDriverCarFormDTO.getCar().getBrand());
        car.setModel(addDriverCarFormDTO.getCar().getModel());
        car.setColor(addDriverCarFormDTO.getCar().getColor());
        car.setPlateNumber(addDriverCarFormDTO.getCar().getPlateNumber());
        car.setFuelType(addDriverCarFormDTO.getCar().getFuelType());
        car.setBodyType(addDriverCarFormDTO.getCar().getBodyType());
        car.setNumOfSeats(addDriverCarFormDTO.getNumOfSeats());
        car.setCurrentLocation(new Location());

        driverAccount.setUser(user);
        driverAccount.setDriverStatus(DriverStatus.BUSY);
        driverAccount.setPicture(null);
        driverAccount.setPhone(addDriverCarFormDTO.getPhone());
        driverAccount.setCar(car);


        final DriversAccount savedAccount = userService.saveDriver(driverAccount);
        return new ResponseEntity(userConverter.toDTO(savedAccount), HttpStatus.OK);

    }

    @PostMapping(value="api/registerConfirm")
    public ResponseEntity register(@RequestBody String email) throws EmailExistException {
        userService.registerConfirm(email);
        return new ResponseEntity(email, HttpStatus.OK);
    }

    @PostMapping(value="api/uploadIMG")
    public ResponseEntity uploadImage(@RequestParam("imageFile") MultipartFile file, @RequestParam("userEmail") String email) throws IOException {


        Image image = imageService.savePicture(file, email);
        image.setPicByte(ImageService.decompressBytes(image.getPicByte()));
        if(image != null)
            return new ResponseEntity(userConverter.toDTO(image), HttpStatus.OK);
        else
            return new ResponseEntity("File not found", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(value="api/acceptBankAccountAccess")
    public ResponseEntity bankAccessAccepted(@RequestBody ClientAccountDTO clientAccountDTO) {

        ClientsAccount client = userService.changeBankStatus(clientAccountDTO.getUser().getEmail(), BankStatus.ACTIVE);


        return new ResponseEntity(userConverter.toDTO(client), HttpStatus.OK);
    }

    @PostMapping(value="api/declineBankAccountAccess")
    public ResponseEntity bankAccessDeclined(@RequestBody ClientAccountDTO clientAccountDTO) {

        ClientsAccount client = userService.changeBankStatus(clientAccountDTO.getUser().getEmail(), BankStatus.EMPTY);


        return new ResponseEntity(userConverter.toDTO(client), HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping(value="api/getAllActiveClients")
    public ResponseEntity getAllActiveClients(){
        return new ResponseEntity(userConverter.toDTOs(userService.getAllActiveClients()), HttpStatus.OK);
    }

}
