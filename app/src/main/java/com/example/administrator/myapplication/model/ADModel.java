package com.example.administrator.myapplication.model;

import android.net.Uri;

public class ADModel {
    private String ID;
    private int play_type;
    private String  image_url;
    private String video_url;
    private int duration;
    private String image_MD5;
    private String media_MD5;
    public ADModel(){
    }
    public ADModel(String ID, int play_type, String image_url, String uri,int duration) {
        this.ID = ID;
        this.play_type = play_type;
        this.image_url = image_url;
        this.video_url = uri;
        this.duration=duration;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getPlay_type() {
        return play_type;
    }

    public void setPlay_type(int play_type) {
        this.play_type = play_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImage_MD5() {
        return image_MD5;
    }

    public void setImage_MD5(String image_MD5) {
        this.image_MD5 = image_MD5;
    }

    public String getMedia_MD5() {
        return media_MD5;
    }

    public void setMedia_MD5(String media_MD5) {
        this.media_MD5 = media_MD5;
    }
}
