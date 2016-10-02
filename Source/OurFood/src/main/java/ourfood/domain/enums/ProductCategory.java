package ourfood.domain.enums;

/**
 * Enum for different product category ()
 *
 * not used at the moment
 *
 * Created by moinhussian.moti on 01-10-2016.
 */
public enum ProductCategory {
    OTHERS(0L);

    Long productCategoryId;

    ProductCategory (Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getProductCategoryId() {
        return this.productCategoryId;
    }
}
