package com.example.administrator.myapplication.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class PlanInfo implements Serializable{
    private String PlanID;
    private List<ADModel> adModelList;
    private int total;  //整个计划的播放总时长 /m
    private int second;  //单个广告播放时长   /s
    public String getPlanID() {
        return PlanID;
    }

    public void setPlanID(String planID) {
        PlanID = planID;
    }

    public List<ADModel> getAdModelList() {
        return adModelList;
    }

    public void setAdModelList(List<ADModel> adModelList) {
        this.adModelList = adModelList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
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
