package ourfood.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Domain object to represent a location of any thing
 * 
 * @author raghu.mulukoju@qnovon.com
 * 
 */
@Embeddable
public class Location implements Serializable {

    @Column
    Float latitude;

    @Column
    Float longitude;

    @Column
    String country;

    @Column
    String state;

    @Column
    String city;

    public Location() {

    }

    public Location(float latitude, float longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
