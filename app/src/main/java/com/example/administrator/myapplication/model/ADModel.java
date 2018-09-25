package com.example.administrator.myapplication.model;

public class ADModel {
    private String ID;
    private int play_type;
    private String image_url;
    private String video_url;
    private int duration;

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
}
