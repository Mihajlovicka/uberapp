package com.example.demo.controller;

import com.example.demo.converter.DriveConverter;
import com.example.demo.dto.CreateDriveReservationDTO;
import com.example.demo.dto.DriveDTO;

import com.example.demo.exception.DriveNotFoundException;
import com.example.demo.exception.EmailNotFoundException;
import com.example.demo.exception.NotDrivePassengerException;
import com.example.demo.exception.TransactionIdDoesNotExistException;
import com.example.demo.model.*;
import com.example.demo.service.DriveService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Map;

@RestController
public class DriveController {
    @Autowired
    DriveService driveService;

    @Autowired
    UserService userService;


    @Autowired
    DriveConverter driveConverter;


    @PostMapping(value="api/acceptDrive/{id}")
    public ResponseEntity acceptDriveParticipation(@PathVariable Long id) throws EmailNotFoundException, DriveNotFoundException, NotDrivePassengerException, URISyntaxException, IOException, TransactionIdDoesNotExistException, InterruptedException {
        return new ResponseEntity(driveConverter.toDTO(driveService.acceptDriveParticipation(id)), HttpStatus.OK);
    }

    @PostMapping(value="api/declineDrive/{id}")
    public ResponseEntity declineDriveParticipation(@PathVariable Long id) throws DriveNotFoundException, NotDrivePassengerException, TransactionIdDoesNotExistException {
        return new ResponseEntity(driveConverter.toDTO(driveService.declineDriveParticipation(id)), HttpStatus.OK);
    }


    @GetMapping(value="api/getDrive/{id}")
    public ResponseEntity<DriveDTO> getDrive(@PathVariable Long id) throws DriveNotFoundException {
        return new ResponseEntity(driveConverter.toDTO(driveService.getDrive(id)), HttpStatus.OK);
    }



    @PostMapping(value = "/api/createDriveReservation")
    public ResponseEntity createDriveReservation(@RequestBody CreateDriveReservationDTO driveReservationDTO) throws EmailNotFoundException, ParseException, URISyntaxException, IOException, InterruptedException {

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
        drive.setOwnerDebit(driveReservationDTO.getOwnerDebit());
        drive.setDriveType(DriveType.FUTURE);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        drive.setDate(format.parse(driveReservationDTO.getDate()));

        final Drive saved = driveService.saveDrive(drive);

        return new ResponseEntity(driveConverter.toDTO(saved), HttpStatus.CREATED);

    }



    @GetMapping(value = "/api/getDrivesClient")
    public ResponseEntity getDrives(@RequestParam String email) throws EmailNotFoundException {


        return new ResponseEntity(driveConverter.toDTOs(driveService.getDrivesForUser(email,false)), HttpStatus.OK);
    }

    @GetMapping(value = "/api/getPastDrivesClient")
    public ResponseEntity getPastDrives(@RequestParam String email) throws EmailNotFoundException {


        return new ResponseEntity(driveService.getDrivesForUser(email,true), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping(path = "/ride/getCurrentDriveDriver")
    public ResponseEntity<Drive> getCurrent(){
        return new ResponseEntity(driveService.getCurrentDrive(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping(path = "/ride/getFirstFuture")
    public ResponseEntity<Map<String, Object>> getFirstFuture(){
        return new ResponseEntity(driveService.getFirstFutureDriveStops(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PostMapping(path = "/ride/endRide")
    public ResponseEntity<String> endRide(){
        return new ResponseEntity(driveService.endDrive(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PostMapping(path = "/ride/goToNextRide")
    public ResponseEntity<List<RealAddress>> goToNextRide(){
        return new ResponseEntity(driveService.goToNextDrive(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PostMapping(path = "/ride/startRide")
    public ResponseEntity<List<RealAddress>> startDrive() throws EmailNotFoundException {
        return new ResponseEntity(driveService.startDrive(), HttpStatus.OK);
    }

    @GetMapping( path = "/api/ride/getNewStartAddress/{id}", produces = "application/json")
    public ResponseEntity<Location> getNewStartAddress(@PathVariable("id") int id) {
        try {
            return new ResponseEntity(driveService.getCarStartEndStop((long) id, true), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping(
            path = "/getClientCurrentDrive",
            produces = "application/json"
    )
    public ResponseEntity<Drive> getClientCurrentDrive() {
        return new ResponseEntity(driveService.getClientCurrentDrive(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PostMapping(path = "/notifyPassengers")
    public ResponseEntity notifyPassengers() throws EmailNotFoundException {
        driveService.notifyPassengers();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PostMapping(path = "/cancelRide")
    public ResponseEntity cancelRide(@RequestBody String reason) throws EmailNotFoundException {
        driveService.cancelDrive(reason);
        return new ResponseEntity<>(null, HttpStatus.OK);

    }

    @GetMapping(value = "/api/getDrive")
    public ResponseEntity getDrive(@RequestParam String driveID) throws EmailNotFoundException {

        int driveId = Integer.valueOf(driveID);
        return new ResponseEntity(driveService.getDrive(driveId), HttpStatus.OK);

    }


    @PostMapping(value="/api/ownerCancelDrive/{id}")
    public ResponseEntity cancelDrive(@PathVariable Long id) throws DriveNotFoundException {
        Drive drive = driveService.getDrive(id);
        return new ResponseEntity(driveConverter.toDTO(driveService.cancelDrive(drive)), HttpStatus.OK);
    }

    @PostMapping(value="/api/continueWithDrive/{id}")
    public ResponseEntity continueWithDrive(@PathVariable Long id) throws DriveNotFoundException, EmailNotFoundException {
        Drive drive = driveService.getDrive(id);
        return new ResponseEntity(driveConverter.toDTO(driveService.continueWithDrive(drive)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping(value = "/getAllDrives")
    public ResponseEntity getAllDrives() {
        return new ResponseEntity(driveConverter.toDTOs(driveService.getAllDrives()), HttpStatus.OK);

    }
}
