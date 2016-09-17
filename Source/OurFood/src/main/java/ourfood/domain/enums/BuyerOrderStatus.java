package ourfood.domain.enums;

/**
 * Buyer order status
 * <p>
 * IMPORTANT: The order of the enums should not be changed
 * </p>
 * 
 * @author raghu.mulukoju
 */
public enum BuyerOrderStatus {
    NONE(0L), ORDERED(1L), PROCESSED(2L), ON_HOLD(3L);

    Long buyerOrderStatusId;

    BuyerOrderStatus(Long buyerOrderStatusId) {
        this.buyerOrderStatusId = buyerOrderStatusId;
    }

    public Long getBuyerOrderStatus() {
        return this.buyerOrderStatusId;
    }
}
