package ourfood.extservice.gcm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GcmParam {

    @JsonProperty("data")
    private GcmData data;

    @JsonProperty("registration_ids")
    private String[] registrationIds;

    public GcmData getData() {
        return data;
    }

    public void setData(GcmData data) {
        this.data = data;
    }

    public void setData(GcmMessage data) {
        this.data = data;
    }

    public String[] getRegistrationIds() {
        return this.registrationIds;
    }

    public void setRegistrationIds(String[] registrationIds) {
        this.registrationIds = registrationIds;
    }
}
