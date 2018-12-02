package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


import com.example.administrator.myapplication.exception.UnCeHandler;
import com.example.administrator.myapplication.utils.L;
import com.example.administrator.myapplication.utils.PreferenceUtil;
import com.liulishuo.filedownloader.FileDownloader;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private String Tag = "MyApplication";
    private ArrayList<Activity> list = new ArrayList<Activity>();
    private int activityCount;//activity的count数
    protected boolean isForeground;//是否在前台

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    public void init() {
        //设置该CrashHandler为程序的默认处理器
        FileDownloader.setup(this);
        UnCeHandler catchExcep = new UnCeHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
        CrashReport.initCrashReport(getApplicationContext(), "a42cc2465b", false);
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        list.add(a);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        activityCount++;
        L.d(Tag, "onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        L.d(Tag, "onActivityStopped");
        activityCount--;
        if (0 == activityCount&& PreferenceUtil.getBoolean(this, "resume", true)) {
            isForeground = false;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                L.e(Tag, "error : " + e.toString());
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setAction(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
