package com.example.demo.service;

import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.model.Drive;
import com.example.demo.model.DriveStatus;
import com.example.demo.model.Passenger;
import com.example.demo.model.User;
import com.example.demo.repository.DriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


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

    public List<Drive> getDrivesForUser(String email) throws EmailNotFoundException {
        List<Drive> drives = new ArrayList<Drive>();
        User user = userService.getByEmail(email);
        for(Drive drive : driveRepository.findAll()){
            if(user.getRole().getName().equals("ROLE_CLIENT")) {
                if (drive.getOwner().getUser().getEmail().equals(email)) {
                    drives.add(drive);
                } else {
                    for (Passenger passenger : drive.getPassengers()) {
                        if (passenger.getPassengerEmail().equals(email)) {
                            drives.add(drive);
                        }
                    }
                }
            }
            else{
                if(drive.getDriver() != null) {
                    if (drive.getDriver().getUser().getEmail().equals(email)) {
                        drives.add(drive);
                    }
                }
            }
        }
        return drives;
    }
}
