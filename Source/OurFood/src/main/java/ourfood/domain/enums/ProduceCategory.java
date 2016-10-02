package ourfood.domain.enums;

/**
 * Different produce category (PULSES/VEGETABLES/FRUITS)
 * <p>
 * IMPORTANT: The order of the enums should not be changed
 * </p>
 * 
 * @author raghu.mulukoju
 */
public enum ProduceCategory {
    OTHERS(0L), PULSES(1L), VEGETABLES(2L), FRUITS(3L), MILLETS(4L), SPICES(5L);

    Long produceCategoryId;

    ProduceCategory(Long produceCategoryId) {
        this.produceCategoryId = produceCategoryId;
    }

    public Long getProduceCategoryId() {
        return this.produceCategoryId;
    }
}
