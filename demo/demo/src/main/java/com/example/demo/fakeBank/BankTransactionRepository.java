package com.example.demo.fakeBank;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {
}
