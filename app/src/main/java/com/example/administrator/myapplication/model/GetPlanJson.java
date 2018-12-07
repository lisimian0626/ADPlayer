package com.example.administrator.myapplication.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class GetPlanJson implements Serializable{
    private String planID;
    private String mac;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    @Override
    public String toString() {
        return toJson();
    }
    private String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
