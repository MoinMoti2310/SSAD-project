package ourfood.extservice.gcm;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Light weight alternative to AppMessagge which is used for push notification
 * 
 * @author mulra02
 *
 */
public class GcmMessage extends GcmData {

    private long id;

    private String title;

    private long actionSetId;

    private String actionSetTitle;

    private String content;

    @JsonProperty("contentFormat")
    private String contentFormat;

    private String status;

    private Date postedTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getActionSetId() {
        return actionSetId;
    }

    public void setActionSetId(long actionSetId) {
        this.actionSetId = actionSetId;
    }

    public String getActionSetTitle() {
        return this.actionSetTitle;
    }

    public void setActionSetTitle(String actionSetTitle) {
        this.actionSetTitle = actionSetTitle;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentFormat() {
        return this.contentFormat;
    }

    public void setContentFormat(String contentFormat) {
        this.contentFormat = contentFormat;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPostedTime() {
        return this.postedTime;
    }

    public void setPostedTime(Date postedTime) {
        this.postedTime = postedTime;
    }

    public String getNotificationType() {
        return GcmData.MESSAGE;
    }
}
