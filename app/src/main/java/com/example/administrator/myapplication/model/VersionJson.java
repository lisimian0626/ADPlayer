package com.example.administrator.myapplication.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class VersionJson implements Serializable{
    private String versionCode;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
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
