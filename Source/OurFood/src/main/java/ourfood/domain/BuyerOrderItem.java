package ourfood.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import ourfood.domain.enums.BuyerOrderItemStatus;
import ourfood.domain.enums.ProduceGrade;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Domain object to represent BuyerOrderItem
 * 
 * @author raghu.mulukoju
 */
@Entity
public class BuyerOrderItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    private Crop crop;

    @NotNull
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    BuyerOrder order;

    @NotNull
    private BuyerOrderItemStatus status;

    private ProduceGrade produceGrade;

    private Long price;

    public BuyerOrderItem() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProduceGrade getProduceGrade() {
        return produceGrade;
    }

    public void setProduceGrade(ProduceGrade produceGrade) {
        this.produceGrade = produceGrade;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}