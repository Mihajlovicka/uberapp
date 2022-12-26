package com.example.demo.controller;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.AddDriverCarFormDTO;
import com.example.demo.dto.RegisterFormDTO;
import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.PlateNumberExistException;
import com.example.demo.model.*;
import com.example.demo.model.Address;
import com.example.demo.model.ClientsAccount;
import com.example.demo.model.DriversAccount;
import com.example.demo.model.User;
import com.example.demo.service.AddressService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserConverter userConverter;

    @Autowired
    RoleService roleService;

    @PostMapping(value="api/register")
    public ResponseEntity register(@RequestBody RegisterFormDTO registerFormDTO) throws EmailExistException {
        User user = new User();
        Address address = new Address();
        ClientsAccount clientsAccount = new ClientsAccount();

        user.setName(registerFormDTO.getName());
        user.setSurname(registerFormDTO.getSurname());
        user.setEmail(registerFormDTO.getEmail());
        user.setPassword(registerFormDTO.getPassword());

        address.setCity(registerFormDTO.getAddress().getCity());
        address.setStreet(registerFormDTO.getAddress().getStreet());
        address.setNumber(registerFormDTO.getAddress().getNumber());


        clientsAccount.setUser(user);
        clientsAccount.setAddress(address);
        clientsAccount.setPicture("");
        clientsAccount.setPhone(registerFormDTO.getPhone());


        user.setRole(roleService.findByName(registerFormDTO.getRole()));
        final ClientsAccount savedAccount = userService.saveClient(clientsAccount);
        return new ResponseEntity(userConverter.toDTO(savedAccount), HttpStatus.OK);


    }


    @PostMapping(value = "api/add-driver")
    public ResponseEntity addDriver(@RequestBody AddDriverCarFormDTO addDriverCarFormDTO) throws EmailExistException, PlateNumberExistException {
        User user = new User();
        Car car = new Car();
        DriversAccount driverAccount = new DriversAccount();

        user.setName(addDriverCarFormDTO.getName());
        user.setSurname(addDriverCarFormDTO.getSurname());
        user.setEmail(addDriverCarFormDTO.getEmail());
        user.setPassword(addDriverCarFormDTO.getPassword());


        car.setBrand(addDriverCarFormDTO.getCar().getBrand());
        car.setModel(addDriverCarFormDTO.getCar().getModel());
        car.setColor(addDriverCarFormDTO.getCar().getColor());
        car.setPlateNumber(addDriverCarFormDTO.getCar().getPlateNumber());
        car.setFuelType(addDriverCarFormDTO.getCar().getFuelType());
        car.setBodyType(addDriverCarFormDTO.getCar().getBodyType());

        driverAccount.setUser(user);
        driverAccount.setPicture("");
        driverAccount.setPhone(addDriverCarFormDTO.getPhone());
        driverAccount.setCar(car);

        user.setRole(roleService.findByName(addDriverCarFormDTO.getRole()));
        final DriversAccount savedAccount = userService.saveDriver(driverAccount);
        return new ResponseEntity(userConverter.toDTO(savedAccount), HttpStatus.OK);

    }

    @PostMapping(value="api/registerConfirm")
    public ResponseEntity register(@RequestBody String email) throws EmailExistException {
        userService.registerConfirm(email);
        return new ResponseEntity(email, HttpStatus.OK);
    }

}
