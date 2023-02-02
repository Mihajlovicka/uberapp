package com.example.demo.controller;

import com.example.demo.converter.DriveConverter;
import com.example.demo.dto.CreateDriveReservationDTO;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.model.Drive;
import com.example.demo.service.DriveService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
public class DriveController {
    @Autowired
    DriveService driveService;

    @Autowired
    UserService userService;

    @Autowired
    DriveConverter driveConverter;

    @PostMapping(value="/api/createDriveReservation")
    public ResponseEntity createDriveReservation(@RequestBody CreateDriveReservationDTO driveReservationDTO) throws EmailNotFoundException, ParseException {
        Drive drive = new Drive();
        drive.setDistance(driveReservationDTO.getDistance());
        drive.setDuration(driveReservationDTO.getDuration());
        drive.setPrice(driveReservationDTO.getPrice());
        drive.setBaby(driveReservationDTO.getBaby());
        drive.setBabySeats(driveReservationDTO.getBabySeats());
        drive.setPets(driveReservationDTO.getPets());
        drive.setSeats(driveReservationDTO.getSeats());
        drive.setRouteJSON(driveReservationDTO.getRouteJSON());
        drive.setSplitBill(driveReservationDTO.isSplitBill());
        drive.setOwner(userService.findClientsAccount(driveReservationDTO.getOwner().getUser().getEmail()));
        //stops
        drive.setStops(driveReservationDTO.getStops());
        //passengeri
        drive.setPassengers(driveReservationDTO.getPassengers());

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        drive.setDate(format.parse(driveReservationDTO.getDate()));

        final Drive saved = driveService.saveDrive(drive);

        return new ResponseEntity(driveConverter.toDTO(saved), HttpStatus.CREATED);

    }


    @GetMapping(value = "/api/getDrivesClient")
    public ResponseEntity getDrives(@RequestParam String email) throws EmailNotFoundException {


        return new ResponseEntity(driveConverter.toDTOs(driveService.getDrivesForUser(email)), HttpStatus.OK);

    }

    @GetMapping(value = "/api/getDrive")
    public ResponseEntity getDrive(@RequestParam String driveID) throws EmailNotFoundException {

        int driveId = Integer.valueOf(driveID);
        return new ResponseEntity(driveService.getDrive(driveId), HttpStatus.OK);

    }
}
