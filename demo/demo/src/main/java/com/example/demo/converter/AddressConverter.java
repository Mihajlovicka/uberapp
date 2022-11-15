package com.example.demo.converter;

import com.example.demo.dto.AddressDTO;
import com.example.demo.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {

    public AddressDTO toDTO(Address address){
        return new AddressDTO(address.getCity(), address.getStreet(), address.getNumber());
    }
}
