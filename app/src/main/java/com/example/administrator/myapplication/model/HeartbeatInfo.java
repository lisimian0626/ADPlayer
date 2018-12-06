package com.example.administrator.myapplication.model;

import java.io.Serializable;

public class HeartbeatInfo implements Serializable{

    private String newPlanID;

    public String getNewPlanID() {
        return newPlanID;
    }

    public void setNewPlanID(String newPlanID) {
        this.newPlanID = newPlanID;
    }
}
