package ourfood.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.NotBlank;

import ourfood.domain.enums.PaymentStatus;
import ourfood.domain.enums.SellerOrderStatus;

/**
 * Domain object to represent SellerOrder
 * 
 * @author raghu.mulukoju
 */
@Entity
public class SellerOrder implements Serializable {

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
    private List<SellerOrderItem> orderItems;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SellerAccount account;

    private SellerOrderStatus status;

    private PaymentStatus paymentStatus;

    private int pinCode;

    public SellerOrder() {

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

    public List<SellerOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<SellerOrderItem> orderItems) {

        if (this.orderItems == null) {
            this.orderItems = new ArrayList<SellerOrderItem>();
        }

        this.orderItems = orderItems;
    }

    public void addOrderItem(SellerOrderItem orderItem) {

        if (this.orderItems == null) {
            this.orderItems = new ArrayList<SellerOrderItem>();
        }
        
        this.orderItems.add(orderItem);
    }

    public SellerAccount getAccount() {
        return account;
    }

    public void setAccount(SellerAccount account) {
        this.account = account;
    }

    public SellerOrderStatus getStatus() {
        return status;
    }

    public void setStatus(SellerOrderStatus status) {
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