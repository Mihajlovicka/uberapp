package com.example.demo.repository;

import com.example.demo.model.ClientsAccount;
import com.example.demo.model.DriverStatus;
import com.example.demo.model.DriversAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DriversRepository extends JpaRepository<DriversAccount, Long> {
    public DriversAccount findDriversAccountByUserEmail(String email);

    @Query("select u from User u where u.status = 'AVAILABLE'")
    List<DriversAccount> findAvailable();

    List<DriversAccount> getByDriverStatus(DriverStatus driverStatus);
}
