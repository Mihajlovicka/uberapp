package com.example.demo.repository;

import com.example.demo.model.Drive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveRepository extends JpaRepository<Drive, Long> {
}
