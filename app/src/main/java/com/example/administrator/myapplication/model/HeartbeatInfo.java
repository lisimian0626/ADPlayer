package com.example.administrator.myapplication.model;

import java.io.Serializable;

public class HeartbeatInfo implements Serializable{

    private String newPlanID;
    private String versionCode;
    private String onTime;
    private String offTime;
    private long FrameFlag;
    public String getNewPlanID() {
        return newPlanID;
    }

    public void setNewPlanID(String newPlanID) {
        this.newPlanID = newPlanID;
    }

    public String getVersioncode() {
        return versionCode;
    }

    public void setVersioncode(String versioncode) {
        versionCode = versioncode;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public long getFrameFlag() {
        return FrameFlag;
    }

    public void setFrameFlag(long frameFlag) {
        FrameFlag = frameFlag;
    }
}
