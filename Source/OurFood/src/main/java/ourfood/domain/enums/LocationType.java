package ourfood.domain.enums;

/**
 * Created by njay on 30/9/16.
 */
public enum LocationType {
    FARM("FARM"),
    WAREHOUSE("WAREHOUSE"),
    MARKET("MARKET");

    private String value;

    LocationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
