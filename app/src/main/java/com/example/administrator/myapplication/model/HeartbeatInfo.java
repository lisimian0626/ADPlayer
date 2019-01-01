package com.example.administrator.myapplication.model;

import java.io.Serializable;

public class HeartbeatInfo implements Serializable{

    private String newPlanID;
    private String versionCode;


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
}
