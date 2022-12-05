package com.example.demo.repository;

import com.example.demo.model.ClientsAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepository extends JpaRepository<ClientsAccount, Long> {
}
