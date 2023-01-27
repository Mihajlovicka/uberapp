package com.example.demo.model;

import com.example.demo.dto.RideSimulationDTO;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class RideSimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String routeJSON;

    @Enumerated(EnumType.STRING)
    @Column
    private RideStatus rideStatus;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    private Car car;

    public RideSimulation(RideSimulationDTO rideDTO) {
        this.id = rideDTO.getId();
        this.routeJSON = rideDTO.getRouteJSON();
        this.rideStatus = rideDTO.getRideStatus();
    }
}
