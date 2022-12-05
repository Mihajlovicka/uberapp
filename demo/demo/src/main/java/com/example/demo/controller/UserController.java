package com.example.demo.controller;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.RegisterFormDTO;
import com.example.demo.exception.ApiRequestException;
import com.example.demo.model.Address;
import com.example.demo.model.ClientsAccount;
import com.example.demo.model.User;
import com.example.demo.service.AddressService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(value = "http://localhost:4200", allowedHeaders="*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserConverter userConverter;

    @PostMapping(value="api/register")
    public ResponseEntity register(@RequestBody RegisterFormDTO registerFormDTO) throws ApiRequestException {
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


        user.setRole(registerFormDTO.getRole());


        //final User savedUser = userService.save(user);
        final ClientsAccount savedAccount = userService.saveClient(clientsAccount);
        return new ResponseEntity(userConverter.toDTO(savedAccount), HttpStatus.OK);

    }

    @PostMapping(value="api/registerConfirm")
    public ResponseEntity register(@RequestBody String email) throws ApiRequestException {
        userService.registerConfirm(email);
        return new ResponseEntity(email, HttpStatus.OK);
    }
}
