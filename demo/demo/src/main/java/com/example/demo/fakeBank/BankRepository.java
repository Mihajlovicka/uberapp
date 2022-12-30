package com.example.demo.fakeBank;


import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<ClientsBankAccount, Long> {

    ClientsBankAccount getClientsBankAccountByAccountNumber(String accountNumber);

}
