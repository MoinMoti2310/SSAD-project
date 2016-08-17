package ourfood.domain.enums;

/**
 * Different types of users (MOBILE/WEB/MOBILEWEB/API)
 * <p>
 * IMPORTANT: The order of the enums should not be changed
 * </p>
 * 
 * @author raghu.mulukoju
 */
public enum UserType {
    NONE(0L), MOBILE(1L), WEB(2L), MOBILEANDWEB(3L), API(4L);

    Long userTypeId;

    UserType(Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    public Long getUserType() {
        return this.userTypeId;
    }
}
