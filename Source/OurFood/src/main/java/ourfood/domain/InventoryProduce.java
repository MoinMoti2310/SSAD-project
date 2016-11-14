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

import ourfood.domain.enums.BuyerOrderItemStatus;
import ourfood.domain.enums.BuyerOrderStatus;
import ourfood.domain.enums.PaymentStatus;
import ourfood.domain.enums.SellerOrderItemStatus;
import ourfood.domain.enums.SellerOrderStatus;

/**
 * Domain object to represent Inventory
 * 
 * @author Bakhtiyar Syed
 */
@Entity
public class InventoryProduce implements Serializable {

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

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private Produce produce;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProcessingCenter warehouse;
    
    
    public InventoryProduce() {

    }

    public ProcessingCenter getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(ProcessingCenter warehouse) {
		this.warehouse = warehouse;
	}
	

	public Produce getProduce() {
		return produce;
	}

	public void setProduce(Produce produce) {
		this.produce = produce;
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

	

	/*public void addOrderItem(SellerOrderItem orderItem) {

        if (this.orderItems == null) {
            this.orderItems = new ArrayList<SellerOrderItem>();
        }
        
        this.orderItems.add(orderItem);
    }*/

   

   
}