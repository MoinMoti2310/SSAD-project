package ourfood.domain;

public enum RoleType {
    ROLE_PLATFORM_POWER_ADMIN, // Platform Power Admin
    ROLE_PLATFORM_PRI_ADMIN, // Platform Primary Admin
    ROLE_ORG_PRI_ADMIN, // Organization Primary Admin
    ROLE_ORG_SEC_ADMIN, // Organization Secondary Admin
    ROLE_BUSINESS_USER, // Individual Business User
    ROLE_MOBILE_USER, // Mobie User
    ROLE_END_USER // Individual End User    
    
    /*Long appMessageTypeId;

    AppMessageType(Long appMessageTypeId) {
        this.appMessageTypeId = appMessageTypeId;
    }

    public Long getappMessageTypeId() {
        return this.appMessageTypeId;
    }*/
}
