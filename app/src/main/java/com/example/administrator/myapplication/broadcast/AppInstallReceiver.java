package com.example.administrator.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.myapplication.common.TConst;
import com.example.administrator.myapplication.utils.L;

import java.io.File;
import java.io.IOException;

public class AppInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {

            //删除apk文件
            deleteApkfile();
        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {

        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {

            deleteApkfile();
        }

    }
    public Boolean deleteApkfile(){
        //Apk文件路径可自定义，此处用 download 文件夹
        File apkfile = new File(TConst.getApkDir() + "/" + TConst.DOWN_APK_NAME + ".apk");
        if (!apkfile.exists()) {

        } else {
            //调用现成的文件操作方法删除文件
            if (apkfile.delete())
                L.test( "apkfile delete successfully! ");
        }
        return false;
    }
}
