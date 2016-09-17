package ourfood.domain.enums;

/**
 * Buyer order item status
 * <p>
 * IMPORTANT: The order of the enums should not be changed
 * </p>
 * 
 * @author raghu.mulukoju
 */
public enum BuyerOrderItemStatus {
    NONE(0L), ORDERED(1L), INSPECTED(2L), ON_HOLD(3L), IN_PROCESSING_CENTER(4L), PROCESSED(5L);

    Long buyerOrderStatusId;

    BuyerOrderItemStatus(Long buyerOrderStatusId) {
        this.buyerOrderStatusId = buyerOrderStatusId;
    }

    public Long getBuyerOrderStatus() {
        return this.buyerOrderStatusId;
    }
}
