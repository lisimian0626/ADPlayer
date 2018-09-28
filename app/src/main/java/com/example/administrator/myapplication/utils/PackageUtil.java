package com.example.administrator.myapplication.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Created by J Wong on 2015/12/7 18:54.
 */
public class PackageUtil {

    public static int getVersionCode(Context context) {
        try {
            int code = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
            return code;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

    public static String getVersionName(Context context) {
        try {
            String name = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String getApplicationName(Context context) {
        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            String applicationName =
                    (String) packageManager.getApplicationLabel(applicationInfo);
            return applicationName;
        } catch (Exception e) {
            L.i("PackageUtil", e.toString());
        }

        return "";
    }

    public static void installApk(Context context, File apkFile, String activityName) {
        Toast.makeText(context, "版本升级中...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setAction("com.ynh.update_apk");
        intent.putExtra("apkname", apkFile.getName());  //abc.apk 为Apk名
        intent.putExtra("packagename", context.getPackageName()); //abcd 为包名
        intent.putExtra("activityname", activityName);
        L.i("installApk", "apk file:" + apkFile.getAbsolutePath() + "  package name:" + context.getPackageName() + "  activityName:" + activityName);
        context.sendBroadcast(intent);
    }

    public static void installApkByApi(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    public static void upgradeSystem(Context context) {
        Intent intent = new Intent("softwinner.intent.action.autoupdate");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void upgradeA20System(Context context) {
        Intent intent = new Intent("softwinner.intent.action.autoupdate");
        intent.putExtra("file", "/mnt/sdcard/update.zip");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public static void toSettingActivity(Context context) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.Settings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    public static void hideSystemUI(Context context, boolean hide) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.hideNaviBar");
        intent.putExtra("hide", hide);
        context.sendBroadcast(intent);
//        getWindow().getDecorView().setSystemUiVisibility(SYS_UI);
    }
}
