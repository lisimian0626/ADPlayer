package com.example.administrator.myapplication.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlanforResult {

    /**
     * WeekPlaylistID : 45
     * ProfileID : 36
     * Layer : 0
     * ProfilePrior : 1
     * StartTime : 2018-11-07,00:00:00
     * EndTime : 2018-12-30,23:59:59
     * Extend1 :
     * Extend2 : 1081
     * Extend3 : 0
     * Extend4 : 0
     * Extend5 :
     */

    private int WeekPlaylistID;
    private int ProfileID;
    private int Layer;
    private int ProfilePrior;
    private String StartTime;
    private String EndTime;
    private String Extend1;
    private int Extend2;
    private int Extend3;
    private int Extend4;
    private String Extend5;
    
    public int getWeekPlaylistID() {
        return WeekPlaylistID;
    }

    public void setWeekPlaylistID(int WeekPlaylistID) {
        this.WeekPlaylistID = WeekPlaylistID;
    }

    public int getProfileID() {
        return ProfileID;
    }

    public void setProfileID(int ProfileID) {
        this.ProfileID = ProfileID;
    }

    public int getLayer() {
        return Layer;
    }

    public void setLayer(int Layer) {
        this.Layer = Layer;
    }

    public int getProfilePrior() {
        return ProfilePrior;
    }

    public void setProfilePrior(int ProfilePrior) {
        this.ProfilePrior = ProfilePrior;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getExtend1() {
        return Extend1;
    }

    public void setExtend1(String Extend1) {
        this.Extend1 = Extend1;
    }

    public int getExtend2() {
        return Extend2;
    }

    public void setExtend2(int Extend2) {
        this.Extend2 = Extend2;
    }

    public int getExtend3() {
        return Extend3;
    }

    public void setExtend3(int Extend3) {
        this.Extend3 = Extend3;
    }

    public int getExtend4() {
        return Extend4;
    }

    public void setExtend4(int Extend4) {
        this.Extend4 = Extend4;
    }

    public String getExtend5() {
        return Extend5;
    }

    public void setExtend5(String Extend5) {
        this.Extend5 = Extend5;
    }
}
