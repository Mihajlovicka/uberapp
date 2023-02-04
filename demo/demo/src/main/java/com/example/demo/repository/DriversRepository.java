package com.example.demo.repository;

import com.example.demo.model.DriverStatus;
import com.example.demo.model.DriversAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriversRepository extends JpaRepository<DriversAccount, Long> {
    public DriversAccount findDriversAccountByUserEmail(String email);


    List<DriversAccount> findByDriverStatus(DriverStatus driverStatus);
}
