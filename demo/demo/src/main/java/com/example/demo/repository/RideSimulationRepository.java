package com.example.demo.repository;

import com.example.demo.model.Car;
import com.example.demo.model.DriversAccount;
import com.example.demo.model.RideSimulation;
import com.example.demo.model.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideSimulationRepository extends JpaRepository<RideSimulation, Integer> {

    List<RideSimulation> findAllByRideStatus(RideStatus rideStatus);

    RideSimulation findByCar(Car car);
}
