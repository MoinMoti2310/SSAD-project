package ourfood.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ourfood.domain.Location;

import javax.persistence.*;

/**
 * Created by njay on 30/9/16.
 */
@Entity
public class Route {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @Access(AccessType.PROPERTY)
    @JsonIgnore
    private Location origin;

    @OneToOne(cascade = CascadeType.ALL)
    private Location destination;

    @Column
    private Double distance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
