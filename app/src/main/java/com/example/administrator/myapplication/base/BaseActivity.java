package com.example.administrator.myapplication.base;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.myapplication.common.TConst;
import com.example.administrator.myapplication.evenbus.BusEvent;
import com.example.administrator.myapplication.evenbus.EventBusHelper;
import com.example.administrator.myapplication.evenbus.EventBusId;
import com.example.administrator.myapplication.model.ADModel;
import com.example.administrator.myapplication.utils.DeviceUtil;
import com.example.administrator.myapplication.utils.L;
import com.example.administrator.myapplication.utils.RxAsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseActivity extends AppCompatActivity {
    private String Tag = "BaseActivity";
    private ScheduledExecutorService mScheduledExecutorService;
    private ScheduledExecutorService mScheduledHreatbeatService;
    public static int nextTime = 20*1000;
    public static int count=0;
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
//        startScreenTimer();
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

    protected void startScreenTimer() {
        if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown()){

        }else{
            mScheduledExecutorService = java.util.concurrent.Executors.newScheduledThreadPool(1);
            scheduleAtFixedRate(mScheduledExecutorService);
        }
        if(mScheduledHreatbeatService != null && !mScheduledHreatbeatService.isShutdown()){

        }else{
            mScheduledHreatbeatService = java.util.concurrent.Executors.newScheduledThreadPool(1);
            scheduleHeartBeat(mScheduledHreatbeatService);
        }
    }

    private void stopScreenTimer() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
            mScheduledExecutorService = null;
        }
        if (mScheduledHreatbeatService != null) {
            mScheduledHreatbeatService.shutdownNow();
            mScheduledHreatbeatService = null;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                count=count+100;
                L.d("nextTime:" + nextTime + "   count:" + count);
                if(count>=nextTime){
                    EventBusHelper.sendEvent(BusEvent.getEvent(EventBusId.nextTime));
                    count=0;
                }else if(count>=100*1000){
                     count=0;
                }

            }
        }, 1000, 100, TimeUnit.MILLISECONDS);
    }

    private void scheduleHeartBeat(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                    EventBusHelper.sendEvent(BusEvent.getEvent(EventBusId.heartbeat));
            }
        }, DeviceUtil.getNum(10), 30+DeviceUtil.getNum(10), TimeUnit.SECONDS);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScreenTimer();
        EventBusHelper.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataWhenOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelLoadingRequest();
    }
    protected String getFiles(){

        return "";
    }
//
//    protected void checkPlan(List<ADModel> adModelListh) {
//        new RxAsyncTask<List<ADModel>, String, List<String>>() {
//            @Override
//            protected List<String> call(List<ADModel>... lists) {
//                for (ADModel adModel : lists[0]) {
//                    File media_file = TConst.getFileByUrl(adModel.getVideo_url());
//                    if (media_file.exists()&&!Filelist.contains(adModel.getVideo_url())) {
//                     Filelist.add(adModel.getVideo_url());
//                    }
//                }
//                return Filelist;
//            }
//
//            @Override
//            protected void onCompleted() {
//
//                super.onCompleted();
//            }
//        }.execute(adModelListh);
//    }


    class freeDiskSpaceTask extends AsyncTask<List<ADModel> ,String ,String > {
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(List<ADModel>... AdModels) {
            freeDiskSpace(AdModels[0]);
            return null;
        }
    }
    public void freeDiskSpace(List<ADModel> adModelList) {

        if (adModelList != null && adModelList.size() > 0) {
            for (ADModel adModel : adModelList) {
                L.d(Tag,"要删除歌曲的初始path:"+str);
                String diskPath = DiskFileUtil.getFileSavedPath(str);
                if (TextUtils.isEmpty(diskPath)) {
                    continue;
                }

                File file = new File(diskPath);
                if (file.exists() && file.isFile()) {
                    file.delete();
                    L.d(Tag,"执行删除路径:"+str);
                }
            }

        }
    }
}
