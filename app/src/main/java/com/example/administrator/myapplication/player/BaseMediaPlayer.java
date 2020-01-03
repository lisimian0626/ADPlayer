package com.example.administrator.myapplication.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseMediaPlayer {

    private MediaPlayer mediaPlayer;
    private OnBasePlayerListener basePlayerListener;
    private String path;
    private SurfaceView surfaceView;
    private ExecutorService executorService;
    private final static String TAG = "BaseMediaPlayer";

    public BaseMediaPlayer(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 播放器监听
     *
     * @param listener 监听
     */
    public void setBasePlayerListener(OnBasePlayerListener listener) {
        this.basePlayerListener = listener;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (surfaceView != null) {
                mediaPlayer.setDisplay(surfaceView.getHolder());
            }
//            mediaPlayer.setLooping(false);
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    Log.i(TAG, "onSeekComplete----" + mediaPlayer.getCurrentPosition());
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    onPlayStart(mediaPlayer,path);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer m) {
                    onPlayCompletion(path);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    onPlayError(path, "what:" + what + " extra:" + extra);
                    return false;
                }
            });
        }
    }

    private void play(String path) {
        this.path = path;
        if (TextUtils.isEmpty(path)) {
            onPlayError(path, "播放路径为空！");
        }
        try {
            Log.i(TAG, "播放：" + path);
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            Log.e(TAG, "读取文件时IO异常", e);
            onPlayError(path, "读取文件时IO异常！");
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.e(TAG, "播放文件时IO异常，url：" + this.path, e);
            onPlayError(path, "播放文件IO异常！");
        }
    }

    public void playMedia(final String path) {
        this.path = path;
        initMediaPlayer();
        play(this.path);
    }


    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        try {
            if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
                this.mediaPlayer.pause();
            }
        } catch (Exception e) {
            Log.e(TAG, "执行“pause()”发生异常：", e);
        }
    }

    /**
     * 播放
     */
    public void start() {
        try {
            if (this.mediaPlayer != null && !this.mediaPlayer.isPlaying()) {
                this.mediaPlayer.start();
            }
        } catch (Exception e) {
            Log.e(TAG, "执行“start()” 发生异常：", e);
        }

    }

    /**
     * 是否正在播放
     *
     * @return 是否正在播放
     */
    public boolean isPlaying() {
        if (this.mediaPlayer != null)
            try {
                return mediaPlayer.isPlaying();
            } catch (Exception e) {
                Log.w(TAG, "isPlaying ex:", e);
            }
        return false;
    }

    /**
     * 当前播放进度，单位毫秒
     *
     * @return 当前播放进度，单位毫秒
     */
    public int getCurrentPosition() {
        try {
            if (isPlaying()) {
                return mediaPlayer.getCurrentPosition();
            }
        } catch (Exception e) {
            Log.e(TAG, "执行“getCurrentPosition()” 发生异常：", e);
        }
        return 0;
    }

    public void creatSuf(SurfaceHolder holder){
        mediaPlayer.setDisplay(holder);
    }
    /**
     * 视频总时长，单位毫秒
     *
     * @return 视频总时长，单位毫秒
     */
    public int getDuration() {
        if (isPlaying()) {
            try {
                return mediaPlayer.getDuration();
            } catch (Exception e) {
                Log.e(TAG, "执行“getDuration()” 发生异常：", e);
            }
        }
        return 0;
    }

    /**
     * 进度拖动
     *
     * @param seek 百分百（0-1）
     */
    public void seekTo(final float seek) {
        try {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mediaPlayer != null) {
                            mediaPlayer.seekTo((int) (seek * getDuration()));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "执行“seekTo()” 发生异常：", e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "执行“seekTo()” 线程发生异常：", e);
        }
    }

    /**
     * 释放
     */
    public void release() {
        try {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.reset();
                this.mediaPlayer.release();
                this.mediaPlayer = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "执行“release()” 发生异常：", e);
        }
    }


    public void onPlayStart(MediaPlayer mp,String path) {
        if (basePlayerListener != null) {
            basePlayerListener.onPlayStart(mp,path);
        }
    }

    public void onPlayCompletion(String path) {
        if (basePlayerListener != null) {
            basePlayerListener.onPlayCompletion(path);
        }
    }

    public void onPlayError(String path, String error) {
        if (basePlayerListener != null) {
            basePlayerListener.onPlayError(path, error);
        }
    }
}