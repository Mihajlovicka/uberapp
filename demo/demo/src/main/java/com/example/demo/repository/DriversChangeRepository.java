package com.example.demo.repository;

import com.example.demo.model.DriversAccount;
import com.example.demo.model.DriversAccountChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriversChangeRepository extends JpaRepository<DriversAccountChange, Long> {

}
