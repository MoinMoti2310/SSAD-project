package ourfood.domain.enums;

/**
 * Different crop category (PULSES/VEGETABLES/FRUITS)
 * <p>
 * IMPORTANT: The order of the enums should not be changed
 * </p>
 * 
 * @author raghu.mulukoju
 */
public enum CropCategory {
    OTHERS(0L), PULSES(1L), VEGETABLES(2L), FRUITS(3L), MILLETS(4L), SPICES(5L);

    Long cropCategoryId;

    CropCategory(Long cropCategoryId) {
        this.cropCategoryId = cropCategoryId;
    }

    public Long getCropCategory() {
        return this.cropCategoryId;
    }
}
