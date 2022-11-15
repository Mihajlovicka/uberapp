package com.example.demo.service;

import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    UserRepository userRepository;


    public User save(User user){
        user.setStatus(Status.NOTACTIVATED);
        return userRepository.save(user);
    }
}
