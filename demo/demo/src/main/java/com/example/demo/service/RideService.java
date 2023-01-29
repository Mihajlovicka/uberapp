package com.example.demo.service;

import com.example.demo.model.FavoriteRide;
import com.example.demo.model.User;
import com.example.demo.repository.FavoriteRidesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {
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
}
