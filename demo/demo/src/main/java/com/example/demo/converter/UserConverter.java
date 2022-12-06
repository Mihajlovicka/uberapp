package com.example.demo.converter;

import com.example.demo.dto.ClientAccountDTO;
import com.example.demo.dto.DriverAccountDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.ClientsAccount;
import com.example.demo.model.DriversAccount;
import com.example.demo.model.User;
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

}
