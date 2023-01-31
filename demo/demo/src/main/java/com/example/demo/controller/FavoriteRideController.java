package com.example.demo.controller;

import com.example.demo.dto.FavoriteRideDTO;
import com.example.demo.model.FavoriteRide;
import com.example.demo.model.RealAddress;
import com.example.demo.service.FavoriteRideService;
import com.example.demo.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class FavoriteRideController {
    @Autowired
    private FavoriteRideService rideService;

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping(path = "/ride/get_favorites", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FavoriteRideDTO>> getFavoriteRides(){
        List<FavoriteRide> rides = rideService.getAllFavorites();
        List<FavoriteRideDTO> ridesDTO = new ArrayList<>();
        rides.forEach(r -> ridesDTO.add(new FavoriteRideDTO(r)));
        return new ResponseEntity<>(ridesDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping(path = "/ride/new_favorite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FavoriteRideDTO> addFavoriteRide(@RequestBody FavoriteRideDTO rideDTO){
        FavoriteRide ride = new FavoriteRide(rideDTO);
        FavoriteRide newRide = rideService.addFavoriteRide(ride);
        FavoriteRideDTO newRideDTO = new FavoriteRideDTO(newRide);
        return new ResponseEntity<>(newRideDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @DeleteMapping(path = "/ride/delete_favorite/{id}")
    public ResponseEntity<String> deleteFavorite(@PathVariable("id") int id){
        rideService.delete(id);
        return new ResponseEntity<>("Uspesno obrisano", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping(path = "/frequentAddresses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RealAddress>> getFrequentAddresses(){
        List<RealAddress> adr = rideService.getFrequentAddresses();
        return new ResponseEntity<>(adr, HttpStatus.OK);
    }
}
