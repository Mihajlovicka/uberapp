package com.example.demo.controller;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.RegisterFormDTO;
import com.example.demo.model.Address;
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

@RestController
@CrossOrigin(value = "http://localhost:4200", allowedHeaders="*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @Autowired
    UserConverter userConverter;

    @PostMapping(value="api/register")
    public ResponseEntity register(@RequestBody RegisterFormDTO registerFormDTO){
        User user = new User();
        Address address = new Address();

        user.setName(registerFormDTO.getName());
        user.setSurname(registerFormDTO.getSurname());
        user.setEmail(registerFormDTO.getEmail());
        user.setPhone(registerFormDTO.getPhone());
        user.setPassword(registerFormDTO.getPassword());

        address.setCity(registerFormDTO.getAddress().getCity());
        address.setStreet(registerFormDTO.getAddress().getStreet());
        address.setNumber(registerFormDTO.getAddress().getNumber());

        user.setAddress(addressService.save(address));

        final User savedUser = userService.save(user);

        return new ResponseEntity(userConverter.toDTO(savedUser), HttpStatus.OK);

    }
}
