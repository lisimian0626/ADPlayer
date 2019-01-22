package com.example.administrator.myapplication.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlanListInfo {

    /**
     * PlaylistID : 1329
     * PlayFileID : 427
     * PlayOrder : 1
     * GroupFlag : 1
     * URL : http://111.230.222.252:8982/file/pic01.png
     */

    private int PlaylistID;
    private int PlayFileID;
    private int PlayOrder;
    private int GroupFlag;
    private int PlaylistSubType;
    private int duration;
    private String FileType;
    private String URL;

    public static List<PlanListInfo> arrayPlanListInfoFromData(String str) {

        Type listType = new TypeToken<ArrayList<PlanListInfo>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public int getPlaylistID() {
        return PlaylistID;
    }

    public void setPlaylistID(int PlaylistID) {
        this.PlaylistID = PlaylistID;
    }

    public int getPlayFileID() {
        return PlayFileID;
    }

    public void setPlayFileID(int PlayFileID) {
        this.PlayFileID = PlayFileID;
    }

    public int getPlayOrder() {
        return PlayOrder;
    }

    public void setPlayOrder(int PlayOrder) {
        this.PlayOrder = PlayOrder;
    }

    public int getGroupFlag() {
        return GroupFlag;
    }

    public void setGroupFlag(int GroupFlag) {
        this.GroupFlag = GroupFlag;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getFilename() {
        return URL.substring(URL.lastIndexOf("/") + 1, URL.length());
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public int getPlaylistSubType() {
        return PlaylistSubType;
    }

    public void setPlaylistSubType(int playlistSubType) {
        PlaylistSubType = playlistSubType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
