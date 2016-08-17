package ourfood.extservice.gcm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoteCommand extends GcmData{

    @JsonProperty("cmd")
    private String command;

    @JsonProperty("cmdData")
    private String commandData;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommandData() {
        return commandData;
    }

    public void setCommandData(String commandData) {
        this.commandData = commandData;
    }
    
    public String getNotificationType() {
        return GcmData.COMMAND;
    }
}
