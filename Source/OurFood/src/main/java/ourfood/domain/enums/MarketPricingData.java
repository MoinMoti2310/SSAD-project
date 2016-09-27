package ourfood.domain.enums;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by njay on 27/9/16.
 */
@Entity
public class MarketPricingData {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    private double minMarketPrice;

    @NotNull
    private double maxMarketPrice;

    @NotNull
    private double modalMarketPrice;

    @NotBlank
    @NotNull
    private String state;

    @NotBlank
    @NotNull
    private String market;

    @NotBlank
    @NotNull
    private String date;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMinMarketPrice() {
        return minMarketPrice;
    }

    public void setMinMarketPrice(double minMarketPrice) {
        this.minMarketPrice = minMarketPrice;
    }

    public double getMaxMarketPrice() {
        return maxMarketPrice;
    }

    public void setMaxMarketPrice(double maxMarketPrice) {
        this.maxMarketPrice = maxMarketPrice;
    }

    public double getModalMarketPrice() {
        return modalMarketPrice;
    }

    public void setModalMarketPrice(double modalMarketPrice) {
        this.modalMarketPrice = modalMarketPrice;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
