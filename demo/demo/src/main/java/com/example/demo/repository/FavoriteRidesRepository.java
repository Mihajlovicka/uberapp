package com.example.demo.repository;

import com.example.demo.model.FavoriteRide;
import com.example.demo.model.RideSimulation;
import com.example.demo.model.RideStatus;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRidesRepository extends JpaRepository<FavoriteRide, Integer> {
    List<FavoriteRide> findByUser(User user);
}
