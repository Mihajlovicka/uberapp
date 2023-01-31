package com.example.demo.model;

import com.example.demo.dto.FavoriteRideDTO;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class FavoriteRide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String routeJSON;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RealAddress> realAddress = new ArrayList<>();

    @ManyToOne
    private User user;

    public FavoriteRide(FavoriteRideDTO ride){
        this.id = ride.getId();
        this.routeJSON = ride.getRouteJSON();
        this.realAddress = ride.getRealAddress();
    }


}
