package com.example.demo.repository;

import com.example.demo.model.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    @Test
    public void shouldFindCarByPlates() {
        Car test = carRepository.findCarByPlateNumber("NS000AB");
        assertThat(test).isNotNull();
    }

    @Test
    public void shouldNotFindCarByPlates() {
        Car test = carRepository.findCarByPlateNumber("NS000AA");
        assertThat(test).isNull();
    }


}
