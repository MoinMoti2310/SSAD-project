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
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import ourfood.domain.enums.SellerOrderItemStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Domain object to represent SellerOrderItem
 * 
 * @author raghu.mulukoju
 */
@Entity
public class SellerOrderItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Produce produce;

    @NotNull
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private
    SellerOrder order;

    @NotNull
    private SellerOrderItemStatus status;

    private Long price;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SellerOrderQuality quality;

    public SellerOrderItem() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produce getProduce() {
        return produce;
    }

    public void setProduce(Produce produce) {
        this.produce = produce;
    }

    public SellerOrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(SellerOrderItemStatus status) {
        this.status = status;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public SellerOrderQuality getQuality() {
        return quality;
    }

    public void setQuality(SellerOrderQuality quality) {
        this.quality = quality;
    }

    public SellerOrder getOrder() {
        return order;
    }

    public void setOrder(SellerOrder order) {
        this.order = order;
    }
}