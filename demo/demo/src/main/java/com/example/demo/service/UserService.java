package com.example.demo.service;

import com.example.demo.exception.ApiRequestException;
import com.example.demo.model.Address;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressService addressService;


    public User save(User user) throws ApiRequestException {
        if(userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new ApiRequestException("Email in use.");
        }
        Address address = addressService.save(user.getAddress());
        user.setStatus(Status.NOTACTIVATED);
        return userRepository.save(user);
    }
}
