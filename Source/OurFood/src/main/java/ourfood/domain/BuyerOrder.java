package ourfood.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import ourfood.domain.enums.BuyerOrderStatus;
import ourfood.domain.enums.PaymentStatus;

/**
 * Domain object to represent BuyerOrder
 * 
 * @author raghu.mulukoju
 */
@Entity
public class BuyerOrder implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private List<BuyerOrderItem> orderItems;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BuyerAccount account;

    private BuyerOrderStatus status;

    private PaymentStatus paymentStatus;

    private int pinCode;

    public BuyerOrder() {

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

    /*
     * public List<BuyerOrderItem> getOrderItems() { return orderItems; }
     * 
     * public void setOrderItems(List<BuyerOrderItem> orderItems) { this.orderItems = orderItems; }
     */
    public List<BuyerOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<BuyerOrderItem> orderItems) {

        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }

        this.orderItems = orderItems;
    }

    public void addOrderItem(BuyerOrderItem orderItem) {

        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }

        this.orderItems.add(orderItem);
    }

    public BuyerAccount getAccount() {
        return account;
    }

    public void setAccount(BuyerAccount account) {
        this.account = account;
    }

    public BuyerOrderStatus getStatus() {
        return status;
    }

    public void setStatus(BuyerOrderStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }
}