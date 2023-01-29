package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Column(name = "latitude", nullable = false)
    private Double latitude =45.238548;

    @Column(name = "longitude", nullable = false)
    private Double longitude = 19.848225 ;
}
