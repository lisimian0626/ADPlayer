package com.example.administrator.myapplication.player;

import android.media.MediaPlayer;
import android.view.SurfaceView;

import com.example.administrator.myapplication.model.ADModel;
import com.example.administrator.myapplication.utils.L;

import java.io.IOException;

/**
 * Created by J Wong on 2017/8/24.
 */

public class BnsPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private SurfaceView mVideoSurfaceView;

    private final static String TAG = "BnsPlayer";

    private String mFilePath;

    private BeidouPlayerListener mBeidouPlayerListener;

    private MediaPlayer mMediaPlayer;

    private boolean isPlaying = false;

    public BnsPlayer(SurfaceView videoSurfaceView, int width, int height) {
        mVideoSurfaceView = videoSurfaceView;
        createMediaPlayer();
    }

    private void createMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    public void playUrl(ADModel adModel) throws IOException {
        stop();
        if (mMediaPlayer == null) {
            createMediaPlayer();
        }
//        open(videoUrl,playmode);
    }

//    private void open(String uri, int playmode) throws IOException {
//        L.d(TAG,"uri:"+DiskFileUtil.getDiskFileByUrl(uri));
//        File file = DiskFileUtil.getDiskFileByUrl(uri);
//            if (file != null) {//存在本地文件
//                Logger.d(TAG, "open local file:" + file.getAbsolutePath());
////                file.delete();
//                mMediaPlayer.setDataSource(file.getAbsolutePath());
//                mMediaPlayer.setDisplay(mVideoSurfaceView.getHolder());
//                mMediaPlayer.prepare();
//                mMediaPlayer.start();
//                isPlaying = true;
//                getTrack(mMediaPlayer);
//
//            } else {//本地文件不存在
//                if(playmode==PREVIEW) {
//                    if (!NetWorkUtils.isNetworkAvailable(Main.mMainActivity.getApplicationContext())) {
//                        return;
//                    }
//                    Logger.d(TAG, "open net file:" + uri);
//                    mMediaPlayer.setDataSource(uri);
//                    mMediaPlayer.setDisplay(mVideoSurfaceView.getHolder());
//                    mMediaPlayer.prepare();
//                    mMediaPlayer.start();
//                    isPlaying = true;
//                    getTrack(mMediaPlayer);
//                }else if(playmode==NORMAL){
//                    Log.e("test","文件不存在");
//                    if(!DiskFileUtil.hasDiskStorage()){
//                        return;
//                    }
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            EventBusUtil.postSticky(EventBusId.id.PLAYER_NEXT, "");
//                        }
//                    },10*1000);
//                    try {
////                        Log.e("test","download:"+ServerFileUtil.getFileUrl(uri));
//                        MyDownloader.getInstance().startDownload(ServerFileUtil.getFileUrl(uri),
//                                DiskFileUtil.getFileSavedPath(uri));
//                    }catch (Exception e){
//                        e.printStackTrace();
//                        Log.e("test","下载失败");
//                    }
//                }
//            }
//
//
//    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        L.e(TAG, "onError  =========================> what:" + what + " extra:" + extra);
//        if (mBeidouPlayerListener != null) {
//            mBeidouPlayerListener.onPlayerCompletion();
//        }
//        BnsAudioRecorder.getInstance().release();
//        NativeScoreRunner.getInstance().stop();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isPlaying = false;
        L.d(TAG, "onCompletion  =========================>");
        if (mBeidouPlayerListener != null) {
            mBeidouPlayerListener.onPlayerCompletion();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        L.d(TAG, "onPrepared  =========================>");
        try {
            if (mBeidouPlayerListener != null)
                mBeidouPlayerListener.onPlayerPrepared();
        } catch (Exception e) {
            L.e(TAG, "onPrepared ex:" + e.toString());
        }


    }


    public int getCurrentPosition() {
        if (isPlaying())
            return mMediaPlayer.getCurrentPosition();
        return 0;
    }

    public int getDuration() {
        if (isPlaying())
            return mMediaPlayer.getDuration();
        return 0;
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null && isPlaying)
            return mMediaPlayer.isPlaying();
        return false;
    }


    public void seekTo(int seek) {
        try {
            if (mMediaPlayer != null)
                mMediaPlayer.seekTo(seek);
        } catch (Exception e) {
            L.e(TAG, "seekTo ex:" + e.toString());
        }
    }


//    public void replay() throws IOException {
//        String url = mFilePath;
//        mFilePath = "";
//        playUrl(url, mRecordFileName,BnsPlayer.NORMAL);
//    }

    public void setBeidouPlayerListener(BeidouPlayerListener listener) {
        mBeidouPlayerListener = listener;
    }


    public void stop() {
        isPlaying = false;
        mMediaPlayer.release();
        mMediaPlayer = null;
        L.d(TAG, "release player");
    }

    public void play() {
        if (mMediaPlayer != null)
            mMediaPlayer.start();
    }

    public void pause() {
        if (mMediaPlayer != null)
            mMediaPlayer.pause();

    }

    public void setVolume(float volume) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(volume, volume);
        }
    }


}
