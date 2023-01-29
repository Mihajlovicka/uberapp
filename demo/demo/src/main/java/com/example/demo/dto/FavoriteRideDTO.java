package com.example.demo.dto;

import com.example.demo.model.FavoriteRide;
import com.example.demo.model.RealAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRideDTO {
    private Integer id;

    private String routeJSON;

    private List<RealAddress> realAddress;

    public FavoriteRideDTO(FavoriteRide ride){
        this.id = ride.getId();
        this.routeJSON = ride.getRouteJSON();
        this.realAddress = ride.getRealAddress();
    }
}
