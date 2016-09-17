package ourfood.domain.enums;

/**
 * Seller order status
 * <p>
 * IMPORTANT: The order of the enums should not be changed
 * </p>
 * 
 * @author raghu.mulukoju
 */
public enum SellerOrderStatus {
    NONE(0L), ORDERED(1L), PROCESSED(2L), ON_HOLD(3L);

    Long sellerOrderStatusId;

    SellerOrderStatus(Long sellerOrderStatusId) {
        this.sellerOrderStatusId = sellerOrderStatusId;
    }

    public Long getSellerOrderStatus() {
        return this.sellerOrderStatusId;
    }
}
