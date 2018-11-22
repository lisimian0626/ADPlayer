package com.example.administrator.myapplication.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.myapplication.evenbus.BusEvent;
import com.example.administrator.myapplication.evenbus.EventBusHelper;
import com.example.administrator.myapplication.evenbus.EventBusId;
import com.example.administrator.myapplication.utils.L;

import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseActivity extends AppCompatActivity {
    private String Tag="BaseActivity";
    private ScheduledExecutorService mScheduledExecutorService;
    public static int syncTime=5;
    public static int nextTime=20;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentView() != View.NO_ID) {
            setContentView(getContentView());
        }
        EventBusHelper.register(this);
        initViews();
        setupToolbar();
        setListener();
        initData();
        startScreenTimer();
    }

    protected void hideToobar() {
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
    }

    protected void hideNavigationBar() {
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
    public abstract @LayoutRes
    int getContentView();
    /**
     * 初始化视图
     */
    public abstract void initViews();

    /**
     * 设置监听器
     */
    public abstract void setListener();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 加载数据(一般用于加载网络数据)
     */
    public abstract void loadDataWhenOnResume();

    /**
     * 取消loading时的操作
     */
    public abstract void cancelLoadingRequest();

    public void setupToolbar() {

    }
       private void startScreenTimer() {
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown())
            return;
        mScheduledExecutorService = java.util.concurrent.Executors.newScheduledThreadPool(1);
        scheduleAtFixedRate(mScheduledExecutorService);

    }

    private void stopScreenTimer() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
            mScheduledExecutorService = null;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long time=System.currentTimeMillis();
                Calendar mCalendar=Calendar.getInstance();
                mCalendar.setTimeInMillis(time);
                int mMinute=mCalendar.get(Calendar.MINUTE);
                int mSecond=mCalendar.get(Calendar.SECOND);
                L.d("minute:"+mMinute+"   second:"+mSecond);
                if(mMinute!=0&&mMinute%syncTime==0&&mSecond==0){
                    L.d("同步");
                    EventBusHelper.sendEvent(BusEvent.getEvent(EventBusId.syncTime));
                    return;
                }else if(mSecond!=0&&mSecond%nextTime==0){
                    L.d("next");
                    EventBusHelper.sendEvent(BusEvent.getEvent(EventBusId.nextTime));
                }else if(mSecond==0){
                    EventBusHelper.sendEvent(BusEvent.getEvent(EventBusId.nextTime));
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScreenTimer();
        EventBusHelper.unregister(this);
    }
}
