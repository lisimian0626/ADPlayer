package com.example.administrator.myapplication.player;

import android.media.MediaPlayer;

public interface OnBasePlayerListener {
    void onPlayStart(MediaPlayer mp,String path);

    void onPlayProgress(String path, long duration, long current);

    void onPlayCompletion(String path);

    void onPlayError(String path, String error);
}
