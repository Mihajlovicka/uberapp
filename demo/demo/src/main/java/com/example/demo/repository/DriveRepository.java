package com.example.demo.repository;

import com.example.demo.model.Drive;
import com.example.demo.model.DriveTimeStatus;
import com.example.demo.model.DriverStatus;
import com.example.demo.model.DriversAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriveRepository extends JpaRepository<Drive, Long> {

    List<Drive> findByDriver(DriversAccount driver);

    List<Drive> findByDriveTimeStatus(DriveTimeStatus driveTimeStatus);
}
