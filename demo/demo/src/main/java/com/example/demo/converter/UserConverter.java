package com.example.demo.converter;

import com.example.demo.dto.ClientAccountDTO;
import com.example.demo.dto.DriverAccountDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    AddressConverter addressConverter;

    @Autowired
    CarConverter carConverter;

    public UserDTO toDTO(User user){
        return new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getStatus(), user.getRole());
    }

    public ClientAccountDTO toDTO(ClientsAccount clientsAccount){
        return new ClientAccountDTO(this.toDTO(clientsAccount.getUser()), addressConverter.toDTO(clientsAccount.getAddress()), clientsAccount.getPicture(), clientsAccount.getPhone());
    }

    public DriverAccountDTO toDTO(DriversAccount driversAccount){
        return new DriverAccountDTO(this.toDTO(driversAccount.getUser()), driversAccount.getPicture(), driversAccount.getPhone(), carConverter.todto(driversAccount.getCar()));
    }

    public ClientsAccount fromDTO(ClientAccountDTO clientAccountDTO){
        ClientsAccount clientsAccount = new ClientsAccount();
        clientsAccount.setUser(new User());
        clientsAccount.setAddress(new Address());
        clientsAccount.getUser().setName(clientAccountDTO.getUser().getName());
        clientsAccount.getUser().setSurname(clientAccountDTO.getUser().getSurname());
        clientsAccount.getUser().setEmail(clientAccountDTO.getUser().getEmail());
        clientsAccount.getUser().setRole(clientAccountDTO.getUser().getRole());
        clientsAccount.getUser().setStatus(clientAccountDTO.getUser().getStatus());


        clientsAccount.getAddress().setCity(clientAccountDTO.getAddress().getCity());
        clientsAccount.getAddress().setStreet(clientAccountDTO.getAddress().getStreet());
        clientsAccount.getAddress().setNumber(clientAccountDTO.getAddress().getNumber());

        clientsAccount.setPhone(clientAccountDTO.getPhone());
        clientsAccount.setPicture(clientAccountDTO.getPicture());

        return clientsAccount;
    }

    public DriversAccount fromDTO(DriverAccountDTO driverAccountDTO){
        DriversAccount driversAccount = new DriversAccount();
        driversAccount.setUser(new User());
        driversAccount.setCar(new Car());
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

        driversAccount.setPhone(driverAccountDTO.getPhone());
        driversAccount.setPicture(driverAccountDTO.getPicture());

        return driversAccount;
    }

}
