package com.example.administrator.myapplication.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class HeartBeatJson implements Serializable{
    private String planID;
    private String AdID;
    private String mac;
    private String versionCode;
    private String playFiles;
    private int captureScreen;
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

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getPlayFiles() {
        return playFiles;
    }

    public void setPlayFiles(String playFiles) {
        this.playFiles = playFiles;
    }

    public int getCaptureScreen() {
        return captureScreen;
    }

    public void setCaptureScreen(int captureScreen) {
        this.captureScreen = captureScreen;
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
