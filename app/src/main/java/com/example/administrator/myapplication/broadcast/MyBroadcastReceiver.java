package com.example.administrator.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.utils.PreferenceUtil;

public class MyBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)&& PreferenceUtil.getBoolean(context, "boot", true)){
            Intent mainIntent=new Intent(context,MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        }
    }
}
