package com.example.demo.service;

import com.example.demo.exception.DriveNotFoundException;
import com.example.demo.exception.EmailNotFoundException;
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

    @Autowired
    NotificationService notificationService;

    public Drive saveDrive(Drive drive) throws EmailNotFoundException {

        //driver se stavlja na null
        drive.setDriver(null);
        //drive startus se stavlja na waiting passengers
        drive.setDriveStatus(DriveStatus.PASSENGERS_WAITING);

        Drive saved = driveRepository.save(drive);


        notificationService.addedToDriveNotify(drive.getPassengers(), saved.getId());


        return saved;

    }

    public Drive getDrive(Long id) throws DriveNotFoundException {
       Drive drive =  driveRepository.findById(id).get();
       if(drive==null)throw new DriveNotFoundException("Drive does not exist!");
       return drive;
    }
}
