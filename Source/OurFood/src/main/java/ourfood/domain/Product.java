package ourfood.domain;

import org.hibernate.validator.constraints.NotBlank;

import ourfood.domain.enums.ProduceCategory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Domain Object to represent Product
 * Created by moinhussian.moti on 01-10-2016.
 */

@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private String img;

    @NotNull
    private ProduceCategory category;

    public Product() {

    }

    public Product(String name, ProduceCategory category) {

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


    public ProduceCategory getProduceCategory() {
        return category;
    }

    public void setProduceCategory(ProduceCategory category) {
        this.category = category;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
