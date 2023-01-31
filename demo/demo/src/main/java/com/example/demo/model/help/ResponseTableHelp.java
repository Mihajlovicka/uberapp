package com.example.demo.model.help;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseTableHelp {
    private ArrayList<ArrayList<Double>> distances;
    private ArrayList<ArrayList<Double>> durations;
}
