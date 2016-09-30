package ourfood.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by njay on 30/9/16.
 */
@Entity
public class MarketPricingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //@OneToOne(fetch = FetchType.LAZY)
    //@Access(AccessType.PROPERTY)
    //@JsonIgnore
    //private Location location;

    @Column
    @NotEmpty
    private Date dateOfArrival;

    @NotEmpty
    @Column
    private Float minPrice;

    @NotEmpty
    @Column
    private Float maxPrice;

    @NotEmpty
    @Column
    private Float modalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    */

    public Date getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(Date dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Float minPrice) {
        this.minPrice = minPrice;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Float getModalPrice() {
        return modalPrice;
    }

    public void setModalPrice(Float modalPrice) {
        this.modalPrice = modalPrice;
    }
}
