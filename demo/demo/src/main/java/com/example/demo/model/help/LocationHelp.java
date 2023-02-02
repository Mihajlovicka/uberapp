package com.example.demo.model.help;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationHelp {
    private ArrayList<ArrayList<Double>> coords;
}
