package ourfood.domain;

import ourfood.domain.enums.LocationType;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Domain object to represent a location of any thing
 * 
 * @author raghu.mulukoju@qnovon.com
 * 
 */
@Entity
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Float latitude;

    @Column
    private Float longitude;

    @Column
    private String state;

    @Column
    private int pinCode;

    @Enumerated(EnumType.STRING)
    private LocationType type;

    public Location() {

    }

    public Location(float latitude, float longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }
}
