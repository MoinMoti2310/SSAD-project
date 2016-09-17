package ourfood.domain.enums;

/**
 * Payment status (NOT_PAID/PAID/PARTIAL_PAID/ON_HOLD)
 * <p>
 * IMPORTANT: The order of the enums should not be changed
 * </p>
 * 
 * @author raghu.mulukoju
 */
public enum PaymentStatus {
    NOT_PAID(0L), PAID(1L), PARTIAL_PAID(2L), ON_HOLD(3L);

    Long paymentStatusId;

    PaymentStatus(Long paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public Long getPaymentStatus() {
        return this.paymentStatusId;
    }
}
