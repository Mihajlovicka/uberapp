package com.example.demo.service;

import com.example.demo.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PassengerService {

    @Autowired
    PassengerRepository passengerRepository;
}
