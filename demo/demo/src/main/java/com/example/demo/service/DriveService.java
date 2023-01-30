package com.example.demo.service;

import com.example.demo.model.Drive;
import com.example.demo.model.DriveStatus;
import com.example.demo.repository.DriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DriveService {
    @Autowired
    DriveRepository driveRepository;

    @Autowired
    UserService userService;

    public Drive saveDrive(Drive drive){

        //driver se stavlja na null
        drive.setDriver(null);
        //drive startus se stavlja na waiting passengers
        drive.setDriveStatus(DriveStatus.PASSENGERS_WAITING);

        return driveRepository.save(drive);

    }
}
