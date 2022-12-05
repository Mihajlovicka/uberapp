package com.example.demo.service;

import com.example.demo.exception.ApiRequestException;
import com.example.demo.model.Address;
import com.example.demo.model.ClientsAccount;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repository.ClientsRepository;
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

    @Autowired
    ClientsRepository clientsRepository;


    public ClientsAccount saveClient(ClientsAccount clientsAccount) throws ApiRequestException {
        if(userRepository.findUserByEmail(clientsAccount.getUser().getEmail()) != null) {
            throw new ApiRequestException("Email in use.");
        }
        final Address address = addressService.save(clientsAccount.getAddress());
        clientsAccount.setAddress(address);

        clientsAccount.getUser().setStatus(Status.NOTACTIVATED);
        final User user = userRepository.save(clientsAccount.getUser());
        clientsAccount.setUser(user);

        return clientsRepository.save(clientsAccount);
    }
}
