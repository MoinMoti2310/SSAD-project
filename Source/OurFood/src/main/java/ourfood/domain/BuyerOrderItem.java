package ourfood.domain;

import java.io.Serializable;

import javax.persistence.*;
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
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Product product;

    @NotNull
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private
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

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BuyerOrder getOrder() {
        return order;
    }

    public void setOrder(BuyerOrder order) {
        this.order = order;
    }

    public BuyerOrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(BuyerOrderItemStatus status) {
        this.status = status;
    }
}