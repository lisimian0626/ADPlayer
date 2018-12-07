package com.example.administrator.myapplication.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class HeartBeatJson implements Serializable{
    private String planID;
    private String AdID;
    private String mac;

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    public String getAdID() {
        return AdID;
    }

    public void setAdID(String adID) {
        AdID = adID;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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
