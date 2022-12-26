package com.example.demo.repository;

import com.example.demo.model.ClientsAccount;
import com.example.demo.model.DriversAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriversRepository extends JpaRepository<DriversAccount, Long> {
    public DriversAccount findDriversAccountByUserEmail(String email);
}
