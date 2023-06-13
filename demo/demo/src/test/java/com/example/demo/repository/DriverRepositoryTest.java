package com.example.demo.repository;

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
public class DriverRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriversRepository driversRepository;

    @Test
    public void shouldFindUserByEmail() {
        User test = userRepository.findUserByEmail("driver@com");
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

    @Test
    public void shouldFindDriverByDriversAvailabilityAndStatus() {
        List<DriversAccount> test = driversRepository.findByDriversAvailability_AndDriverStatus(true, DriverStatus.AVAILABLE);
        assertThat(test).isNotEmpty();
    }

    @Test
    public void shouldNotFindDriverByDriversAvailabilityAndStatus() {
        List<DriversAccount> test = driversRepository.findByDriversAvailability_AndDriverStatus(true, DriverStatus.BUSY);
        assertThat(test).isEmpty();
    }

    @Test
    public void shouldDriverUserByEmail() {
        DriversAccount test = driversRepository.findDriversAccountByUserEmail("driver@com");
        assertThat(test).isNotNull();
    }

    @Test
    public void shouldNotDriverUserByEmail() {
        DriversAccount test = driversRepository.findDriversAccountByUserEmail("@gmail.com");
        assertThat(test).isNull();
    }
}
