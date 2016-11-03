package com.syx.doubleservice;

/**
 * Created by Administrator on 2016/7/20.
 */
public class PushDataModel {
    public String ID;
    public String CreateTime;
    public String DStatus;
    public String MessageContent;
    public String ValidityTime;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getDStatus() {
        return DStatus;
    }

    public void setDStatus(String DStatus) {
        this.DStatus = DStatus;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }

    public String getValidityTime() {
        return ValidityTime;
    }

    public void setValidityTime(String validityTime) {
        ValidityTime = validityTime;
    }

    @Override
    public String toString() {
        return "PushDataModel{" +
                "ID='" + ID + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", DStatus='" + DStatus + '\'' +
                ", MessageContent='" + MessageContent + '\'' +
                ", ValidityTime='" + ValidityTime + '\'' +
                '}';
    }
}
