package ourfood.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import ourfood.domain.enums.CropCategory;

/**
 * Domain object to represent Crop
 * 
 * @author raghu.mulukoju
 */
@Entity
public class Crop implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotBlank
    private String name;

    private String description;
    
    @NotNull
    private CropCategory category;

    public Crop() {

    }

    public Crop(String name, CropCategory category) {

        this.name = name;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CropCategory getCropCategory() {
        return category;
    }

    public void setCropCategory(CropCategory category) {
        this.category = category;
    }
}