package com.example.administrator.myapplication;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();

//            Logger.i(TAG, "initPlayer");
//            mTvPlayerPause.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    ChooseSongs chooseSongs = ChooseSongs.getInstance(getApplicationContext());
//                    if (chooseSongs.getChooseSize() > 0) {
//                        playSong(chooseSongs.getFirstSong());
//                    } else {
//                        Logger.d(TAG, " play public  ad  initPlayer");
//                        playVideoAdRandom();
//                    }
//                    int count = Integer.valueOf(chooseSongs.getChooseSize());
//                    mTvChooseCount.setText(String.valueOf(count));
//                    updatePlayingText();
//                }
//            }, 1000);


    }
}
