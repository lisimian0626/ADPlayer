package com.example.administrator.myapplication;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.base.BaseActivity;
import com.example.administrator.myapplication.evenbus.BusEvent;
import com.example.administrator.myapplication.evenbus.EventBusId;
import com.example.administrator.myapplication.evenbus.PlayerEvent;
import com.example.administrator.myapplication.model.ADModel;
import com.example.administrator.myapplication.setting.SettingDialog;
import com.example.administrator.myapplication.utils.AssetsUtils;
import com.example.administrator.myapplication.utils.L;
import com.example.administrator.myapplication.utils.MultiScreenUtils;
import com.example.administrator.myapplication.utils.UIUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends BaseActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private final String TAG = "MainActivity";
    MediaPlayer mediaPlayer;
    List<ADModel> list_Ad;
    LinearLayout lin_mode1;
    ImageView iv_pic;
    SurfaceView main_surf1;
    //    private RelativeLayout layout_main;
    int current_play = 0;
    private TextView tv_tips;
    private boolean isSurfaceCreated = false;        //surface是否已经创建好
    private int curIndex = 0;
    private String curID = "";
    private boolean isPlaying = false;
    private long mExitTime = 0;
    private static final int MAX_EXIT_INTERVAL = 2000;
    private SettingDialog settingDialog;
    private void play(ADModel adModel) {
        stopPlayer();
        syncTime = 4;
        nextTime = 20;
        if (adModel == null) {
            return;
        }
        tv_tips.setText("当前播放：" + "广告" + adModel.getID() + "|" + "模板" + adModel.getPlay_type());
        iv_pic.setVisibility(adModel.getPlay_type() == 2 ? View.VISIBLE : View.GONE);
        if (mediaPlayer == null) {
            CreateMediaPlayer();
        }
        if (curID.equals(adModel.getID())) {
            if (curIndex > 0) {
                mediaPlayer.seekTo(curIndex);
                curIndex = 0;
            }
            mediaPlayer.start();
        } else {
            try {
                open(adModel.getVideo_url());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        switch (adModel.getPlay_type()) {
            case 1:
                break;
            case 2:

                iv_pic.setImageResource(adModel.getImage_url());
                mediaPlayer.setDisplay(main_surf1.getHolder());
                break;
        }
        curID = adModel.getID();
    }

    private void initPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        CreateMediaPlayer();
        // 把输送给surfaceView的视频画面，直接显示到屏幕上,不要维持它自身的缓冲区
        CreateSurfaceView();
    }

    private void CreateMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    private void CreateSurfaceView() {
//        main_surf2.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        main_surf1.getHolder().setKeepScreenOn(true);
        main_surf1.getHolder().addCallback(new SurfaceCallback());
    }

    private void stopPlayer() {
        isPlaying = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 暂停
     */
    private void Pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerEvent(PlayerEvent event) {
//        BaseDownloadTask task = FileDownloader.getImpl().create(mSong.getMp3FilePath())
//                .setPath(Constant.File.getMusicDir()+ mSong.getMusicFilename());
//        mTaskList.add(task);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(BusEvent event) {
        switch (event.id) {
            case EventBusId.syncTime:
                L.d("收到同步");
//                Pause();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                curIndex = 0;
                current_play = 0;
                play(list_Ad.get(current_play));
                break;
            case EventBusId.nextTime:
                if (current_play < list_Ad.size() - 1) {
                    current_play++;
                } else {
                    current_play = 0;
                }
                play(list_Ad.get(current_play));
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        L.d(TAG, "onCompletion");
        isPlaying=false;
//        if (current_play < list_Ad.size() - 1) {
//            current_play++;
//        } else {
//            current_play = 0;
//        }
//        play(list_Ad.get(current_play));
    }

    /**
     * 页面从前台到后台会执行 onPause ->onStop 此时Surface会被销毁，
     * 再一次从后台 到前台时需要 重新创建Surface
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isSurfaceCreated) {
            CreateSurfaceView();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        hideToobar();
        play();

    }

    private void play() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSurfaceCreated)
                    play(list_Ad.get(current_play));
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Pause();
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        stopPlayer();
        initPlayer();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        tv_tips = findViewById(R.id.tv_tips);
        lin_mode1 = findViewById(R.id.lin_mode);
        iv_pic = findViewById(R.id.iv_pic);
        main_surf1 = findViewById(R.id.main_surf);
    }

    @Override
    public void setListener() {
        MyApplication application = (MyApplication) getApplication();
        application.init();
        tv_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetting();
            }
        });
    }

    @Override
    public void initData() {
        list_Ad = Arrays.asList(
                new ADModel("1", 2, R.drawable.pic01, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01), 0),
                new ADModel("2", 2, R.drawable.pic02, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio02), 0),
                new ADModel("3", 2, R.drawable.pic03, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio03), 0),
                new ADModel("4", 2, R.drawable.pic04, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio04), 0),
                new ADModel("5", 2, R.drawable.pic05, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio05), 0),
                new ADModel("6", 2, R.drawable.pic06, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio06), 0),
                new ADModel("7", 2, R.drawable.pic07, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio07), 0),
                new ADModel("8", 2, R.drawable.pic08, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio08), 0),
                new ADModel("9", 2, R.drawable.pic09, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio09), 0),
                new ADModel("10", 2, R.drawable.pic10, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio10), 0)

        );
        initPlayer();
    }

    @Override
    public void loadDataWhenOnResume() {

    }

    @Override
    public void cancelLoadingRequest() {

    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        /**
         * 创建SurfaceView时开始从上次位置播放或重新播放
         *
         * @param holder
         */
        public void surfaceCreated(SurfaceHolder holder) {
            isSurfaceCreated = true;
            mediaPlayer.setDisplay(holder);
        }

        /**
         * 离开SurfaceView时停止播放，保存播放位置
         */
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 隐藏view的时候销毁SurfaceHolder的时候记录当前的播放位置并停止播放
            isSurfaceCreated = false;
            if (mediaPlayer.isPlaying()) {
                curIndex = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            }
        }
    }

    private void open(Uri uri) throws IOException {
//        File file = new File(uri.toString());
//        if (file != null) {//存在本地文件
//            L.d("open local file:" + file.getAbsolutePath());
        mediaPlayer.setDataSource(MainActivity.this, uri);
        mediaPlayer.setDisplay(main_surf1.getHolder());
        mediaPlayer.prepare();
        isPlaying = true;
//        }else{
//            L.d("文件不存在");
//        }
    }
    public void showSetting() {
        if ((System.currentTimeMillis() - mExitTime) > MAX_EXIT_INTERVAL) {
            mExitTime = System.currentTimeMillis();
        } else {
            if(settingDialog==null){
                settingDialog=new SettingDialog();
            }
            if(!settingDialog.isAdded()) {
                settingDialog.show(getSupportFragmentManager(), "setting");
            }
        }
    }
}
