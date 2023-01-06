package com.example.demo.repository;

import com.example.demo.model.ClientsAccount;
import com.example.demo.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientsRepository extends JpaRepository<ClientsAccount, Long> {
    public ClientsAccount findClientsAccountByUserEmail(String email);

    public List<ClientsAccount> findAllByUserStatus(Status status);
}
