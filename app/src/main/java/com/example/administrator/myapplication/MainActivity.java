package com.example.administrator.myapplication;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.administrator.myapplication.evenbus.BusEvent;
import com.example.administrator.myapplication.evenbus.PlayerEvent;
import com.example.administrator.myapplication.model.ADModel;
import com.example.administrator.myapplication.utils.AssetsUtils;
import com.example.administrator.myapplication.utils.L;
import com.example.administrator.myapplication.utils.UIUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener{
    private final String TAG="MainActivity";
    MediaPlayer mediaPlayer;
    List<ADModel> list_Ad;
    LinearLayout lin_mode1,lin_mode2;
    ImageView iv_pic;
    SurfaceView main_surf1,main_surf2;
    int current_play=0;
    private TextView tv_tips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_main);
        tv_tips=findViewById(R.id.tv_tips);
        lin_mode1=findViewById(R.id.lin_mode1);
        lin_mode2=findViewById(R.id.lin_mode2);
        iv_pic=findViewById(R.id.iv_pic);
        main_surf1=findViewById(R.id.main_surf);
        main_surf2=findViewById(R.id.main_surf2);
        MyApplication application= (MyApplication) getApplication();
        application.init();
        list_Ad= Arrays.asList(
                new ADModel("1",2,R.drawable.lan01,R.raw.lan_video01,0),
//                new ADModel("2",2,R.drawable.lan02,R.raw.lan_video02,0),
                new ADModel("3",2,R.drawable.lan03,R.raw.lan_video03,0),
                new ADModel("4",2,R.drawable.lan04,R.raw.lan_video04,0),
                new ADModel("5",2,R.drawable.lan05,R.raw.lan_video05,0)
        );
        initPlayer();
    }
    private void play(ADModel adModel){
        if(adModel==null){
            return;
        }
        tv_tips.setText("当前播放："+"广告"+adModel.getID()+"|"+"模板"+adModel.getPlay_type());
        lin_mode1.setVisibility(adModel.getPlay_type()==1? View.VISIBLE:View.GONE);
        lin_mode2.setVisibility(adModel.getPlay_type()==2?View.VISIBLE:View.GONE);
        iv_pic.setVisibility(adModel.getPlay_type()==2?View.VISIBLE:View.GONE);
        switch (adModel.getPlay_type()){
            case 1:
//                try {
//                    mediaPlayer.setDataSource(adModel.getVideo_url());
                    mediaPlayer.reset();
                    mediaPlayer=MediaPlayer.create(MainActivity.this,adModel.getVideo_url());
                    mediaPlayer.setDisplay(main_surf1.getHolder());
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.setOnErrorListener(this);
//                    mediaPlayer.prepare();
                    mediaPlayer.start();
//                    mediaPlayer.setDisplay(main_surf1.getHolder());
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    L.e(TAG,"IOException:"+e.toString());
//                }
                break;
            case 2:
                iv_pic.setImageResource(adModel.getImage_url());
                mediaPlayer.reset();
                mediaPlayer=MediaPlayer.create(MainActivity.this,adModel.getVideo_url());
                mediaPlayer.setDisplay(main_surf2.getHolder());
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setOnErrorListener(this);
//                    mediaPlayer.prepare();
                mediaPlayer.start();
                break;
        }

    }
    private void initPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            play(list_Ad.get(0));
            }
        },1000);
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerEvent(PlayerEvent event) {
//        BaseDownloadTask task = FileDownloader.getImpl().create(mSong.getMp3FilePath())
//                .setPath(Constant.File.getMusicDir()+ mSong.getMusicFilename());
//        mTaskList.add(task);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(current_play<list_Ad.size()-1){
            current_play++;
        }else{
            current_play=0;
        }
        play(list_Ad.get(current_play));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopPlayer();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        stopPlayer();
        initPlayer();
        return false;
    }
    private void hideSystemUI(boolean hide) {
        UIUtils.hideNavibar(getApplicationContext(), hide);
    }
    public void hideNavigationBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE;//0x00001000; // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        try {
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
