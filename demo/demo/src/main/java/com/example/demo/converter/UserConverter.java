package com.example.demo.converter;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    AddressConverter addressConverter;

    public UserDTO toDTO(User user){
        return new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhone(), addressConverter.toDTO(user.getAddress()), user.getStatus(), user.getRole());
    }

}
