package com.example.administrator.myapplication.model;

import android.net.Uri;

public class ADModel {
    private String ID;
    private int play_type;
    private int image_url;
    private Uri video_url;
    private int duration;

    public ADModel(String ID, int play_type, int image_url, Uri uri, int duration) {
        this.ID = ID;
        this.play_type = play_type;
        this.image_url = image_url;
        this.video_url = uri;
        this.duration = duration;
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

    public int getImage_url() {
        return image_url;
    }

    public void setImage_url(int image_url) {
        this.image_url = image_url;
    }

    public Uri getVideo_url() {
        return video_url;
    }

    public void setVideo_url(Uri video_url) {
        this.video_url = video_url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
