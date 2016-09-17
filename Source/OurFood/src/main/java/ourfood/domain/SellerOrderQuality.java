package ourfood.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Domain object to represent Crop
 * 
 * @author raghu.mulukoju
 */
@Entity
public class SellerOrderQuality implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    private Float moisture;

    private Float grainSize;

    private Float millerIndex;

    private String image;

    public SellerOrderQuality() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMoisture() {
        return moisture;
    }

    public void setMoisture(Float moisture) {
        this.moisture = moisture;
    }

    public Float getGrainSize() {
        return grainSize;
    }

    public void setGrainSize(Float grainSize) {
        this.grainSize = grainSize;
    }

    public Float getMillerIndex() {
        return millerIndex;
    }

    public void setMillerIndex(Float millerIndex) {
        this.millerIndex = millerIndex;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}