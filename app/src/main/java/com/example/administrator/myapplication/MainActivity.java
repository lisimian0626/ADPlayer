package com.example.administrator.myapplication;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;

import com.example.administrator.myapplication.evenbus.BusEvent;
import com.example.administrator.myapplication.evenbus.PlayerEvent;
import com.example.administrator.myapplication.model.ADModel;
import com.example.administrator.myapplication.utils.AssetsUtils;
import com.example.administrator.myapplication.utils.L;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lin_mode1=findViewById(R.id.lin_mode1);
        lin_mode2=findViewById(R.id.lin_mode2);
        iv_pic=findViewById(R.id.iv_pic);
        main_surf1=findViewById(R.id.main_surf);
        main_surf2=findViewById(R.id.main_surf2);
        MyApplication application= (MyApplication) getApplication();
        application.init();
        list_Ad= Arrays.asList(
                new ADModel("1",2,R.drawable.lan01,"android.resource://" + "com.example.administrator.myapplication" + "/" +R.raw.lan_video01,0),
                new ADModel("2",2,R.drawable.lan02,"android.resource://" + "com.example.administrator.myapplication" + "/" +R.raw.lan_video02,0),
                new ADModel("3",2,R.drawable.lan03,"android.resource://" + "com.example.administrator.myapplication" + "/" +R.raw.lan_video03,0),
                new ADModel("4",2,R.drawable.lan04,"android.resource://" + "com.example.administrator.myapplication" + "/" +R.raw.lan_video04,0),
                new ADModel("5",2,R.drawable.lan05,"android.resource://" + "com.example.administrator.myapplication" + "/" +R.raw.lan_video05,0)
        );
        initPlayer();
    }
    private void play(ADModel adModel){
        if(adModel==null){
            return;
        }
        lin_mode1.setVisibility(adModel.getPlay_type()==1? View.VISIBLE:View.GONE);
        lin_mode2.setVisibility(adModel.getPlay_type()==2?View.VISIBLE:View.GONE);
        iv_pic.setVisibility(adModel.getPlay_type()==2?View.VISIBLE:View.GONE);
        switch (adModel.getPlay_type()){
            case 1:
                try {
                    mediaPlayer.setDataSource(adModel.getVideo_url());
                    mediaPlayer.setDisplay(main_surf1.getHolder());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    L.e(TAG,"IOException:"+e.toString());
                }
                break;
            case 2:
                iv_pic.setImageResource(adModel.getImage_url());
                try {
                    mediaPlayer.setDataSource(adModel.getVideo_url());
                    mediaPlayer.setDisplay(main_surf2.getHolder());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    L.e(TAG,"IOException:"+e.toString());
                }
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
        },5000);
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerEvent(PlayerEvent event) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(current_play<list_Ad.size()){
            current_play++;
            play(list_Ad.get(current_play));
        }else{
            current_play=0;
            play(list_Ad.get(current_play));
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        stopPlayer();
        initPlayer();
        return false;
    }
}
