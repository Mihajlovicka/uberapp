package com.example.demo.model;

import com.example.demo.dto.CarSimulationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String plateNumber;

    @Column(nullable = true)
    private CarBodyType bodyType;

    @Column(nullable = true)
    private Fuel fuelType;

    @Column(nullable=false)
    private int numOfSeats;

    @Embedded
    private Location currentLocation;

    @Column
    private boolean carOnMap = false;

    public Car(Long id, String brand, String model, String color, String plateNumber, CarBodyType bodyType, Fuel fuelType, int numOfSeats){
       this.id = id;
       this.brand = brand;
       this.model = model;
       this.color = color;
       this.plateNumber = plateNumber;
       this.bodyType = bodyType;
       this.fuelType = fuelType;
       this.numOfSeats = numOfSeats;
    }

    public Car(CarSimulationDTO carDTO){
        this.id = Long.valueOf(carDTO.getId());
        this.plateNumber = carDTO.getLicensePlateNumber();
        this.currentLocation = new Location(carDTO.getLatitude(), carDTO.getLongitude());
    }

}
