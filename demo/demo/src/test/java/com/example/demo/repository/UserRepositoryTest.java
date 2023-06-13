package com.example.demo.repository;

import com.example.demo.model.Drive;
import com.example.demo.model.DriverStatus;
import com.example.demo.model.DriversAccount;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriversRepository driversRepository;

    @Test
    public void shouldFindUserByEmail() {
        User test = userRepository.findUserByEmail("klijent2@com");
        assertThat(test).isNotNull();
    }

    @Test
    public void shouldNotFindUserByEmail() {
        User test = userRepository.findUserByEmail("@gmail.com");
        assertThat(test).isNull();
    }

    @Test
    public void shouldFindDriverByDriverStatus() {
        List<DriversAccount> test = driversRepository.findByDriverStatus(DriverStatus.AVAILABLE);
        assertThat(test).isNotEmpty();
    }

    @Test
    public void shouldReturnEmptyListDriverByDriverStatus() {
        List<DriversAccount> test = driversRepository.findByDriverStatus(DriverStatus.GOING_TO_LOCATION);
        assertThat(test).isEmpty();
    }

}
