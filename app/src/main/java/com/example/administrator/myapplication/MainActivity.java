package com.example.administrator.myapplication;

import android.app.smdt.SmdtManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.myapplication.base.BaseActivity;
import com.example.administrator.myapplication.bussiness.constract.MainConstract;
import com.example.administrator.myapplication.bussiness.persent.MainPresenter;
import com.example.administrator.myapplication.common.TConst;
import com.example.administrator.myapplication.evenbus.BusEvent;
import com.example.administrator.myapplication.evenbus.EventBusHelper;
import com.example.administrator.myapplication.evenbus.EventBusId;
import com.example.administrator.myapplication.evenbus.PlayerEvent;
import com.example.administrator.myapplication.model.ADModel;
import com.example.administrator.myapplication.model.GetPlanJson;
import com.example.administrator.myapplication.model.HeartBeatJson;
import com.example.administrator.myapplication.model.HeartbeatInfo;
import com.example.administrator.myapplication.model.PlanInfo;
import com.example.administrator.myapplication.model.PlanListInfo;
import com.example.administrator.myapplication.model.PlanforResult;
import com.example.administrator.myapplication.model.PlanlistJson;
import com.example.administrator.myapplication.model.VersionJson;
import com.example.administrator.myapplication.model.VersionResult;
import com.example.administrator.myapplication.net.download.DownloadQueueHelper;
import com.example.administrator.myapplication.service.SleepService;
import com.example.administrator.myapplication.setting.SettingDialog;
import com.example.administrator.myapplication.upgrade.dialog.DlgProgress;
import com.example.administrator.myapplication.utils.DeviceUtil;
import com.example.administrator.myapplication.utils.L;
import com.example.administrator.myapplication.utils.PackageUtil;
import com.example.administrator.myapplication.utils.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;


public class MainActivity extends BaseActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MainConstract.MainView {
    private final String TAG = "MainActivity";
    MediaPlayer mediaPlayer;
    LinearLayout lin_mode1;
    ImageView iv_pic;
    SurfaceView main_surf1;
    private CameraView cameraView;
    //    private RelativeLayout layout_main;
    int current_play = 0;
    private String cur_ADId = "";
    private TextView tv_version, tv_cupchip, tv_tips, tv_process;
    private boolean isSurfaceCreated = false;        //surface是否已经创建好
    private int curIndex = 0;
    private boolean isPlaying = false;
    private boolean isDownLoading = false;
    private long mExitTime = 0;
    private static final int MAX_EXIT_INTERVAL = 2000;
    private SettingDialog settingDialog;
    private List<BaseDownloadTask> mTaskList = new ArrayList<>();
    private byte[] temp;
    private List<ADModel> adModelList;
    private ADModel cur_Ad;
    private MainPresenter mainPresenter;
    private PlanInfo curPlanInfo;
    private String cur_planID, new_planID;
    private DlgProgress dlgProgress;
    private SmdtManager smdt;
    private RequestOptions options;
    private void play(ADModel adModel) {
        stopPlayer();
        if (adModel == null) {
            return;
        }

        cur_Ad = adModel;
        tv_tips.setText("当前播放：" + "广告" + adModel.getID() + "|" + "模板" + adModel.getPlay_type());
        cameraView.setVisibility(adModel.getPlay_type() == 1 ? View.VISIBLE : View.GONE);
        if (mediaPlayer == null) {
            CreateMediaPlayer();
        }
        if (!isSurfaceCreated) {
            CreateSurfaceView();
        }
        if (TextUtils.isEmpty(adModel.getVideo_url())) {
            main_surf1.setVisibility(View.GONE);
        } else {
            File media_file = TConst.getFileByUrl(adModel.getVideo_url());
            if (media_file == null) {
                main_surf1.setVisibility(View.GONE);
            } else {
                if (media_file.exists()) {
                    main_surf1.setVisibility(View.VISIBLE);
                    try {
                        nextTime=adModel.getDuration();
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(media_file.getAbsolutePath());
                        mediaPlayer.setDisplay(main_surf1.getHolder());
                        mediaPlayer.setLooping(true);
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (adModel.getVideo_url() != null) {
                        startDownload(adModel.getVideo_url());
                    }
                }
            }
        }
        if (TextUtils.isEmpty(adModel.getImage_url())) {
            iv_pic.setVisibility(View.GONE);
        } else {
            File image_file = TConst.getFileByUrl(adModel.getImage_url());
            if (image_file == null) {
                iv_pic.setVisibility(View.GONE);
            } else {
                if (image_file.exists()) {
                    iv_pic.setVisibility(View.VISIBLE);
                    if (options == null) {
                        options = initOption();
                    }
                    Glide.with(this).load(image_file).apply(options).into(iv_pic);
                } else {
                    if (adModel.getImage_url() == null)
                        return;
                    startDownload(adModel.getImage_url());
                }
            }
        }


//        }
//        switch (adModel.getPlay_type()) {
//            case 1:
//                break;
//            case 2:
//                File image_file = TConst.getFileByUrl(adModel.getImage_url());
//                if (image_file == null)
//                    return;
//                if (image_file.exists()) {
//                    if(options==null){
//                        options=initOption();
//                    }
//                    Glide.with(this).load(image_file).apply(options).into(iv_pic);
//                } else {
//                    if (adModel.getImage_url() == null)
//                        return;
//                    startDownload(adModel.getImage_url());
//                }
////                iv_pic.setImageResource(Integer.valueOf(adModel.getVideo_url()));
//                break;
//        }
        cur_ADId = adModel.getID();
        nextTime=adModel.getDuration();
    }

    private void initPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        CreateMediaPlayer();

    }

    private void CreateMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    private void CreateSurfaceView() {
//        main_surf2.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        main_surf1.setVisibility(View.VISIBLE);
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
        switch (event.getId()) {
            case PlayerEvent.TYPE_DOWNLOADCOMPLITE:
                cur_planID = new_planID;
                String jsonMeal = PreferenceUtil.getString(MainActivity.this, "planInfo", "");
                if (!TextUtils.isEmpty(jsonMeal)) {
                    Gson gson = new Gson();
                    PlanInfo planInfo = gson.fromJson(jsonMeal, PlanInfo.class);
                    if (planInfo != null && planInfo.getAdModelList().size() > 0) {
                        initPlan(planInfo);
                        play();
                    }
                }

                break;
        }

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
                play();
                break;
            case EventBusId.nextTime:
                if (adModelList == null)
                    return;
                if (current_play < adModelList.size() - 1) {
                    current_play++;
                } else {
                    current_play = 0;
                }
                play(adModelList.get(current_play));
                break;
            case EventBusId.heartbeat:
                HeartBeatJson heartBeatJson = new HeartBeatJson();
                if (cur_Ad != null) {
                    heartBeatJson.setAdID(cur_Ad.getID());
                    heartBeatJson.setPlanID(String.valueOf(cur_Ad.getPlay_type()));
                }
                heartBeatJson.setMac(DeviceUtil.getCupChipID());
                heartBeatJson.setVersionCode(String.valueOf(PackageUtil.getVersionCode(MainActivity.this)));
//                heartBeatJson.setMac("e558779714542319");
                L.test("heartBeatJson:" + heartBeatJson.toString());
                mainPresenter.fetchHeartbeat(heartBeatJson.toString());
                break;
            case EventBusId.startCamera:
//                cameraView.setPreviewRotation(180);
                cameraView.startCamera();
                break;

            case EventBusId.startHDMI:
                break;

            case EventBusId.closeCamera:
                cameraView.stopCamera();
                break;
            case EventBusId.closeHDMI:
                break;

        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        L.d(TAG, "onCompletion");
        isPlaying = false;
    }

    /**
     * 页面从前台到后台会执行 onPause ->onStop 此时Surface会被销毁，
     * 再一次从后台 到前台时需要 重新创建Surface
     */
    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(new Intent(MainActivity.this, SleepService.class));
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
        if (!isSurfaceCreated) {
            CreateSurfaceView();
        }
        play();
//        L.d("mac:"+DeviceUtil.getCupChipID());

//        File file = new File(TConst.getApkDir(), "test");

    }

    private void startDownload(final List<PlanListInfo> planListInfos) {
        isDownLoading = true;
        tv_process.setVisibility(View.VISIBLE);
        mTaskList.clear();
        for (PlanListInfo info : planListInfos) {
            File file = new File(TConst.getApkDir(), info.getFilename());
            if (!file.exists()) {
                BaseDownloadTask task = FileDownloader.getImpl().create(info.getURL())
                        .setPath(TConst.getApkDir() + info.getFilename());
                mTaskList.add(task);
            }
        }
        if (mTaskList.size() == 0) {
            //下载完成
            isDownLoading = false;
            tv_process.setVisibility(View.GONE);
            EventBusHelper.sendDownComplite();
        }
        DownloadQueueHelper.getInstance().downloadSequentially(mTaskList);
        DownloadQueueHelper.getInstance().setOnDownloadListener(new DownloadQueueHelper.OnDownloadListener() {
            @Override
            public void onDownloadComplete(BaseDownloadTask task) {
                L.test("Complete");
            }

            @Override
            public void onDownloadTaskError(BaseDownloadTask task, Throwable e) {
                if (task == null || task.getFilename() == null) {
                    tv_process.setText("文件下载失败!");
                } else {
                    tv_process.setText(task.getFilename() + "   文件下载失败!");
                }
                L.test("加载文件失败,请重新下载！");
            }

            @Override
            public void onDownloadProgress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task == null || task.getFilename() == null) {
                    return;
                }
                tv_process.setText(task.getFilename() + "   文件下载中...");
//                tv_process.setText(task.getFilename() + "  " + String.format("%.2f", (float) soFarBytes / totalBytes) + "% 文件下载中...");
                L.test((soFarBytes / totalBytes) * 100 + "   " + "sofar:" + soFarBytes + "    total:" + totalBytes);
            }

            @Override
            public void onDownloadTaskOver() {
                L.test("下载完成");
                isDownLoading = false;
                tv_process.setVisibility(View.GONE);

                EventBusHelper.sendDownComplite();
            }
        });
    }

    private void startDownload(String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl)) {
            return;
        }
        isDownLoading = true;
        tv_process.setVisibility(View.VISIBLE);
        mTaskList.clear();
        File file = TConst.getFileByUrl(downloadUrl);
        if (!file.exists()) {
            BaseDownloadTask task = FileDownloader.getImpl().create(downloadUrl)
                    .setPath(TConst.getApkDir() + TConst.getFileNameByUrl(downloadUrl));
            mTaskList.add(task);
        }

        if (mTaskList.size() == 0) {
            //下载完成
            isDownLoading = false;
            tv_process.setVisibility(View.GONE);
        }
        DownloadQueueHelper.getInstance().downloadSequentially(mTaskList);
        DownloadQueueHelper.getInstance().setOnDownloadListener(new DownloadQueueHelper.OnDownloadListener() {
            @Override
            public void onDownloadComplete(BaseDownloadTask task) {
                L.test("Complete");
            }

            @Override
            public void onDownloadTaskError(BaseDownloadTask task, Throwable e) {
                if (task == null || task.getFilename() == null) {
                    tv_process.setText("文件下载失败!");
                }
                tv_process.setText(task.getFilename() + "   文件下载失败!");
                L.test("加载文件失败,请重新下载！");
            }

            @Override
            public void onDownloadProgress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task == null || task.getFilename() == null) {
                    return;
                }
                tv_process.setText(task.getFilename() + "   文件下载中...");
//                tv_process.setText(task.getFilename() + "  " + String.format("%.2f", (float) soFarBytes / totalBytes) + "% 文件下载中...");
                L.test((soFarBytes / totalBytes) * 100 + "   " + "sofar:" + soFarBytes + "    total:" + totalBytes);
            }

            @Override
            public void onDownloadTaskOver() {
                L.test("下载完成");
                isDownLoading = false;
                tv_process.setVisibility(View.GONE);
            }
        });
    }

    private PlanInfo data2PlanInfo(List<PlanListInfo> planListInfos) {
        Map<Integer, List<PlanListInfo>> maplist = new HashMap<>();
        for (PlanListInfo planListInfo : planListInfos) {
//            TextUtils.isEmpty(planListInfo.getDuration())
            //使用GSON，直接转成Bean对象
            List<PlanListInfo> temp = maplist.get(planListInfo.getGroupFlag());
            if (temp == null) {
                temp = new ArrayList<>();
                temp.add(planListInfo);
                maplist.put(planListInfo.getGroupFlag(), temp);
            } else {
                /*某个sku之前已经存放过了,则直接追加数据到原来的List里**/
                temp.add(planListInfo);
            }
        }
        PlanInfo planInfo = new PlanInfo();
        List<ADModel> adModelList = new ArrayList<>();
        for (Integer i : maplist.keySet()) {
            List<PlanListInfo> plan = maplist.get(i);
            ADModel adModel = new ADModel();
            for (PlanListInfo data : plan) {
                if (data.getFileType().equals("PNG")) {
                    adModel.setID(String.valueOf(data.getGroupFlag()));
                    adModel.setImage_url(data.getURL());
                    adModel.setPlay_type(data.getPlaylistSubType());
                    adModel.setDuration(TConst.normal_duration);
                } else if (data.getFileType().equals("AVI")) {
                    adModel.setID(String.valueOf(data.getGroupFlag()));
                    adModel.setVideo_url(data.getURL());
                    adModel.setPlay_type(data.getPlaylistSubType());
                    adModel.setDuration(TConst.normal_duration);
                }
            }
            adModelList.add(adModel);
        }
        Collections.sort(adModelList, new Comparator<ADModel>() {
            @Override
            public int compare(ADModel adModel, ADModel t1) {
                int i = 0;
                try {
                    i = Integer.valueOf(adModel.getID()) - Integer.valueOf(t1.getID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return i;
            }
        });
        planInfo.setSecond(20);
        planInfo.setTotal(4);
        planInfo.setPlanID(new_planID);
        planInfo.setAdModelList(adModelList);
        return planInfo;
    }

    private void play() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSurfaceCreated && adModelList != null && adModelList.size() > 0)
                    play(adModelList.get(current_play));
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
//        EventBusHelper.sendEvent(BusEvent.getEvent(EventBusId.nextTime));
//        initPlayer();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        adModelList.get(current_play).setDuration(mp.getDuration()/1000);
        L.test("duration:" + nextTime);
//        mp.getDuration();
        mediaPlayer.seekTo(curIndex);
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smdt = new SmdtManager(MainActivity.this);
        if (options == null) {
            options = initOption();
        }
//        startService(new Intent(MainActivity.this,SleepService.class));
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        mainPresenter = new MainPresenter(this);
        tv_tips = findViewById(R.id.tv_tips);
        tv_process = findViewById(R.id.tv_process);
        tv_version = findViewById(R.id.tv_version);
        tv_cupchip = findViewById(R.id.tv_cupchip);
        lin_mode1 = findViewById(R.id.lin_mode);
        iv_pic = findViewById(R.id.iv_pic);
        main_surf1 = findViewById(R.id.main_surf);
        cameraView = findViewById(R.id.view_bottom);
        tv_version.setText(PackageUtil.getVersionName(MainActivity.this));
        String Chip = DeviceUtil.getCupChipID();
        if (!TextUtils.isEmpty(Chip)) {
            tv_cupchip.setText("SN:" + Chip.substring(Chip.length() - 4, Chip.length()));
        }
        initPlayer();
        startScreenTimer();
        String planjson = PreferenceUtil.getString(MainActivity.this, "planInfo", "");
        if (!TextUtils.isEmpty(planjson)) {

            Gson gson = new Gson();
            PlanInfo planInfo = gson.fromJson(planjson, PlanInfo.class);
            L.test(planInfo.toString());
            if (planInfo != null && planInfo.getAdModelList().size() > 0) {
                initPlan(planInfo);
            }
        }
//        cameraView.setPreviewResolution(CAMERA_VIEW_WIDTH,CAMERA_VIEW_HEIGHT);
    }

    @Override
    public void setListener() {
        MyApplication application = (MyApplication) getApplication();
        application.init();
        tv_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetting();
            }
        });
        cameraView.setPreviewCallback(new CameraView.PreviewCallback() {
            @Override
            public void onGetYuvFrame(byte[] data) {
                temp = data;
                Log.e("lin", "===lin===>  onGetYuvFrame");
            }
        });
    }

    @Override
    public void initData() {
        String onTime=PreferenceUtil.getString(MainActivity.this, "onTime", "");
        String offTime=PreferenceUtil.getString(MainActivity.this, "offTime", "");
        if (!TextUtils.isEmpty(onTime) &&!TextUtils.isEmpty(offTime)){
            SmdtManager smdt = SmdtManager.create(this);
            smdt.smdtSetTimingSwitchMachine (offTime, onTime,"1");
            L.test("setTime------"+"offTime:"+offTime+"----"+"onTime:"+onTime);
        }
        cameraView.setPreviewResolution(1080,650);
//
//        int screen_number = smdt.getHdmiinStatus();
//        L.test("screen_number:"+String.valueOf(screen_number));

//        else{
//            iv_pic.setImageResource(R.drawable.ad_corner_default);
//        }
        if (PreferenceUtil.getBoolean(MainActivity.this, "camera", true)) {
//            cameraView.setPreviewRotation(180);

            boolean camera=cameraView.startCamera();
            L.test("camera:"+camera);
        }
    }

//    private PlanInfo getDefPlan() {
//        PlanInfo planInfo = new PlanInfo();
//        List<ADModel> list_Ad;
//        list_Ad = Arrays.asList(
//                new ADModel("1", 2, String.valueOf(R.drawable.pic01), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01))),
//                new ADModel("1", 2, String.valueOf(R.drawable.pic02), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01))),
//                new ADModel("1", 2, String.valueOf(R.drawable.pic03), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01))),
//                new ADModel("1", 2, String.valueOf(R.drawable.pic04), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01))),
//                new ADModel("1", 2, String.valueOf(R.drawable.pic05), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01))),
//                new ADModel("1", 2, String.valueOf(R.drawable.pic01), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01))),
//                new ADModel("1", 2, String.valueOf(R.drawable.pic01), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01))),
//                new ADModel("1", 2, String.valueOf(R.drawable.pic01), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01))),
//                new ADModel("1", 2, String.valueOf(R.drawable.pic01), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01))),
//                new ADModel("1", 2, String.valueOf(R.drawable.pic01), String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01)))
//        );
//        planInfo.setPlanID("0");
//        planInfo.setAdModelList(list_Ad);
//        planInfo.setSecond(20);
//        planInfo.setTotal(4);
//        L.test("planinfo:" + planInfo.toString());
//        return planInfo;
//    }


    private void initPlan(PlanInfo planInfo) {
        if (planInfo != null && !TextUtils.isEmpty(planInfo.getPlanID()) && planInfo.getAdModelList() != null && planInfo.getAdModelList().size() > 0) {
            cur_planID = planInfo.getPlanID();
            syncTime = planInfo.getTotal();
            nextTime = planInfo.getSecond();
            adModelList = planInfo.getAdModelList();
//            adModelList.get(0).setPlay_type(1);
//            adModelList.get(1).setPlay_type(1);
//            adModelList.get(0).setVideo_url(null);
//            adModelList.get(1).setImage_url(null);
        }

    }

    @Override
    public void loadDataWhenOnResume() {

    }

    @Override
    public void cancelLoadingRequest() {

    }


    @Override
    public void onFeedBack(boolean success, String key, Object data) {

    }

    @Override
    public void OnHeartbeat(ResponseBody responseBody) {
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        try {
            String heartbeat=responseBody.string();
            L.test("heartbeat:"+heartbeat);
            JsonArray jsonArray = parser.parse(heartbeat).getAsJsonArray();
            Gson gson = new Gson();
            ArrayList<HeartbeatInfo> heartbeatInfoArrayList = new ArrayList<>();

            //加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                //使用GSON，直接转成Bean对象
                HeartbeatInfo heartbeatInfo = gson.fromJson(user, HeartbeatInfo.class);
                heartbeatInfoArrayList.add(heartbeatInfo);
            }

            String version = heartbeatInfoArrayList.get(0).getVersioncode();
            if (Integer.valueOf(version) > PackageUtil.getVersionCode(MainActivity.this)) {
                L.test("服务器有新版本");
                VersionJson versionJson = new VersionJson();
                versionJson.setVersionCode(version);
                L.test(versionJson.toString());
                mainPresenter.fetchApkInfo(versionJson.toString());
            }
            String off=heartbeatInfoArrayList.get(0).getOffTime();
            String on=heartbeatInfoArrayList.get(0).getOnTime();
            if(!TextUtils.isEmpty(off)&&!TextUtils.isEmpty(on)){
                if(!PreferenceUtil.getString(MainActivity.this, "offTime", "").equals(off)||!PreferenceUtil.getString(MainActivity.this, "onTime", "").equals(on)){
                    PreferenceUtil.setString(MainActivity.this, "offTime", off);
                    PreferenceUtil.setString(MainActivity.this, "onTime", on);
                    SmdtManager smdt = SmdtManager.create(this);
                    smdt.smdtSetTimingSwitchMachine (off, on,"1");
                    L.test("setTimeOnHeartBeat------"+"offTime:"+off+"----"+"onTime:"+on);
                }
            }

            new_planID = heartbeatInfoArrayList.get(0).getNewPlanID();
            if (!TextUtils.isEmpty(new_planID) && !new_planID.equals(cur_planID)) {
                GetPlanJson getPlanJson = new GetPlanJson();
                getPlanJson.setPlanID(new_planID);
                getPlanJson.setMac(DeviceUtil.getCupChipID());
                L.test(getPlanJson.toString());
                mainPresenter.fetchPlan(getPlanJson.toString());
            }
            int total=0;
            long frameFlag=heartbeatInfoArrayList.get(0).getFrameFlag();
            for (ADModel adModel:adModelList){
                total+=adModel.getDuration();
            }
            if(frameFlag!=0){
                long time=frameFlag%total;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OngetPlan(ResponseBody responseBody) {
        try {
//            L.test("OngetPlan:"+responseBody.string());
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(responseBody.string()).getAsJsonArray();
            Gson gson = new Gson();
            ArrayList<PlanforResult> planforResultArrayList = new ArrayList<>();

            //加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                //使用GSON，直接转成Bean对象
                PlanforResult planforResult = gson.fromJson(user, PlanforResult.class);
                planforResultArrayList.add(planforResult);
            }
            int profileID = planforResultArrayList.get(0).getProfileID();
            //    getPlaylist/{"playlistID":"36","total":"30","mac":"e558779714542319"}
            PlanlistJson planlistJson = new PlanlistJson();
            planlistJson.setPlaylistID(String.valueOf(profileID));
            planlistJson.setMac(DeviceUtil.getCupChipID());
//            planlistJson.setMac("e558779714542319");
            L.test(planlistJson.toString());
            mainPresenter.fetctPlanList(planlistJson.toString());
//            L.test(planforResultArrayList.get(0).getProfileID()+"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnGetPlanList(ResponseBody responseBody) {
        try {
//                        L.test("OnGetPlanList:"+responseBody.string());
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(responseBody.string()).getAsJsonArray();
            Gson gson = new Gson();
            List<PlanListInfo> planListInfos = new ArrayList<>();
            //加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                //使用GSON，直接转成Bean对象
                PlanListInfo planListInfo = gson.fromJson(user, PlanListInfo.class);
                planListInfos.add(planListInfo);
            }
            PreferenceUtil.setString(MainActivity.this, "planInfo", data2PlanInfo(planListInfos).toString());
            if (!isDownLoading) {
                startDownload(planListInfos);
            }
//            L.test("url:"+url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnGetApkInfo(ResponseBody responseBody) {
        try {
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(responseBody.string()).getAsJsonArray();
            Gson gson = new Gson();
            List<VersionResult> versionResults = new ArrayList<>();
            //加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                //使用GSON，直接转成Bean对象
                VersionResult versionResult = gson.fromJson(user, VersionResult.class);
                versionResults.add(versionResult);
            }
            if (dlgProgress == null) {
                dlgProgress = new DlgProgress(MainActivity.this);
                dlgProgress.setTitle("版本升级");
                dlgProgress.setTip("版本升级中，请勿关机...");
                dlgProgress.show();
                dlgProgress.startDownload(versionResults.get(0).getApkurl());
            }

//            L.test("url:"+url);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if (settingDialog == null) {
                settingDialog = new SettingDialog();
            }
            if (!settingDialog.isAdded()) {
                settingDialog.show(getSupportFragmentManager(), "setting");
            }
        }
    }

    private RequestOptions initOption() {
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return options;
    }
//    private boolean SetHdmi(Boolean enable) {
//        boolean ret1 = smdt.setHdmiInAudioEnable(getApplicationContext(), enable);
//        if (ret1) {
//            Toast.makeText(getApplicationContext(), "hdmi audio open success!", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getApplicationContext(), "hdmi audio open failed!", Toast.LENGTH_SHORT).show();
//        }
//        return ret1;
//    }

    public void changeAppBrightness(float brightness) {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
//        if (brightness == -1) {
//            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
//        } else {
//            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
//        }
        lp.screenBrightness = brightness;
        window.setAttributes(lp);
    }

    private int getSystemBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }
}
