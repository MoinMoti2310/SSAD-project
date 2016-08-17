package ourfood.extservice.sms;

public class SmsParam {

    private String[] registrationIds;
    private String registrationIdsStr;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String[] getRegistrationIds() {
        return this.registrationIds;
    }

    public void setRegistrationIds(String[] registrationIds) {
        this.registrationIds = registrationIds;
    }
    
    public String getRegistrationIdsStr(){
        return registrationIdsStr;
    }
    
    public void setRegistrationIdsStr(String registrationIdsStr){
        this.registrationIdsStr = registrationIdsStr;
    }
}
