package com.example.demo.converter;

import com.example.demo.dto.DriveDTO;
import com.example.demo.model.Drive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class DriveConverter {

    @Autowired
    UserConverter userConverter;

    public DriveDTO toDTO(Drive drive){
        DriveDTO dto = new DriveDTO();
        dto.setId(drive.getId());
        dto.setBaby(drive.getBaby());
        dto.setBabySeats(drive.getBabySeats());
        dto.setDistance(drive.getDistance());
        dto.setDuration(drive.getDuration());
        dto.setPrice(drive.getPrice());
        if(drive.getDriver()!=null){
            dto.setDriver(userConverter.toDTO(drive.getDriver()));
        }

        dto.setOwner(userConverter.toDTO(drive.getOwner()));
        dto.setRouteJSON(drive.getRouteJSON());
        dto.setDriveStatus(drive.getDriveStatus());
        dto.setStops(drive.getStops());
        dto.setPassengers(drive.getPassengers());
        dto.setSeats(drive.getSeats());
        dto.setPets(drive.getPets());
        dto.setSplitBill(drive.isSplitBill());
        dto.setOwnerDebit(drive.getOwnerDebit());

        dto.setDriveType(drive.getDriveType());

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        if(drive.getStartDate()!=null)
            dto.setStartDate(dateFormat.format(drive.getStartDate()));
        if(drive.getEndDate()!=null)
            dto.setEndDate(dateFormat.format(drive.getEndDate()));
        dto.setDate(dateFormat.format(drive.getDate()));
        dto.setOwnerTransactionId(drive.getOwnerTransactionId());

        return dto;
    }

    public List<DriveDTO> toDTOs(List<Drive> drives) {
        List<DriveDTO> driveDTOs = new ArrayList<DriveDTO>();
        for(Drive drive : drives){
            driveDTOs.add(this.toDTO(drive));
        }
        return driveDTOs;
    }
}
