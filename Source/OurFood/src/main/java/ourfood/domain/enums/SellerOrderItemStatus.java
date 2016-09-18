package ourfood.domain.enums;

/**
 * Seller order item status
 * <p>
 * IMPORTANT: The order of the enums should not be changed
 * </p>
 * 
 * @author raghu.mulukoju
 */
public enum SellerOrderItemStatus {
    NONE(0L), ORDERED(1L), INSPECTED(2L), IN_PROCESSING_CENTER(3L), PROCESSED(4L), ON_HOLD(5L);

    Long sellerOrderStatusId;

    SellerOrderItemStatus(Long sellerOrderStatusId) {
        this.sellerOrderStatusId = sellerOrderStatusId;
    }

    public Long getSellerOrderStatus() {
        return this.sellerOrderStatusId;
    }
}
