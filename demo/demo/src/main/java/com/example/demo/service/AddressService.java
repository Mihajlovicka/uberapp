package com.example.demo.service;

import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    public Address save(Address address){
        return addressRepository.save(address);
    }
}
