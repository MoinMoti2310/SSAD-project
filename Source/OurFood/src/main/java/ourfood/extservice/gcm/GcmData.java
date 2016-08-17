package ourfood.extservice.gcm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes({ @JsonSubTypes.Type(value = GcmMessage.class, name = "gcmMsg"),
        @JsonSubTypes.Type(value = RemoteCommand.class, name = "remoteCmd") })
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GcmData {

    public static String MESSAGE = "MESSAGE";
    public static String COMMAND = "COMMAND";
    public static String BLANK = "BLANK";

    private String notificationType;

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
