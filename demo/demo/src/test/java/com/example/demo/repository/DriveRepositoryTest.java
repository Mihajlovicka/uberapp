package com.example.demo.repository;


import com.example.demo.model.Drive;
import com.example.demo.model.DriveType;
import com.example.demo.model.DriversAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")

public class DriveRepositoryTest {

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private DriversRepository driversRepository;


    @Test
    @Sql("classpath:test-data-2.sql")
    public void shouldFindDriveByType() {
        List<Drive> test = driveRepository.findByDriveType(DriveType.NOW);
        assertThat(test).isNotEmpty();
    }

    @Test
    @Sql("classpath:test-data-2.sql")
    public void shouldReturnEmptyListDriveByType() {
        List<Drive> test = driveRepository.findByDriveType(DriveType.FUTURE);
        assertThat(test).isEmpty();
    }

    @Test
    @Sql("classpath:test-data-2.sql")
    public void shouldFindDriveByDriver() {
        DriversAccount driver = driversRepository.findById(Long.valueOf(1)).orElse(null);
        List<Drive> test = driveRepository.findByDriver(driver);
        assertThat(test).isNotEmpty();
    }

    @Test
    @Sql("classpath:test-data-2.sql")
    public void shouldReturnEmptyListFindDriveByDriver() {
        DriversAccount driver = driversRepository.findById(Long.valueOf(9)).orElse(null);
        List<Drive> test = driveRepository.findByDriver(driver);
        assertThat(test).isEmpty();
    }

    
    @Test
    @Sql("classpath:test-data-3.sql")
    public void shouldFindDriveByItsOwnerUserEmailAndOwnerTransactionalId(){
        Drive drive = driveRepository.findByOwner_User_EmailAndOwnerTransactionId("jelenamanojlovic27062000@gmail.com", Long.valueOf(1));

        assertThat(drive).isNotNull();
    }

    @Test
    @Sql("classpath:test-data-3.sql")
    public void driveNotFoundEmailCorrectOwnerTransactionIdWrong(){
        Drive drive = driveRepository.findByOwner_User_EmailAndOwnerTransactionId("jelenamanojlovic27062000@gmail.com", Long.valueOf(3));
        assertThat(drive).isNull();
    }

    @Test
    @Sql("classpath:test-data-3.sql")
    public void driveNotFoundEmailWrongOwnerTransactionIdCorrect(){
        Drive drive = driveRepository.findByOwner_User_EmailAndOwnerTransactionId("nepostojeciEmail@gmail.com", Long.valueOf(1));
        assertThat(drive).isNull();
    }

    @Test
    @Sql("classpath:test-data-3.sql")
    public void driveNotFoundEmailWrongOwnerTransactionIdWrong(){
        Drive drive = driveRepository.findByOwner_User_EmailAndOwnerTransactionId("nepostojeciEmail@gmail.com", Long.valueOf(3));
        assertThat(drive).isNull();
    }


}
