package com.example.administrator.myapplication.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlanlistJson {


    /**
     * playlistID : 36
     * total : 30
     * mac : e558779714542319
     */

    private String playlistID;
    private String total;
    private String mac;

    public String getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(String playlistID) {
        this.playlistID = playlistID;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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
