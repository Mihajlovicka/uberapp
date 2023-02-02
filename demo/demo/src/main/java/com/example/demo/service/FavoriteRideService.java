package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.help.ResponseTableHelp;
import com.example.demo.repository.FavoriteRidesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteRideService {
    @Autowired
    private FavoriteRidesRepository favoriteRidesRepository;
    @Autowired
    private UserService userService;

    public List<FavoriteRide> getAllFavorites() {
        User user = userService.getLoggedIn();
        return favoriteRidesRepository.findByUser(user);
    }

    public FavoriteRide addFavoriteRide(FavoriteRide ride) {
        User user = userService.getLoggedIn();
        ride.setUser(user);
        return favoriteRidesRepository.save(ride);
    }

    public void delete(int id) {
        favoriteRidesRepository.deleteById(id);
    }


    public List<RealAddress> getFrequentAddresses() {
        List<FavoriteRide> rides = getAllFavorites();
        List<RealAddress> addresses = new ArrayList<>();
        for(FavoriteRide r: rides){
            for(RealAddress a:r.getRealAddress()){
                if(!addresses.contains(a)) addresses.add(a);
            }
        }
        return addresses;
    }
}



