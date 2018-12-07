package com.example.administrator.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.base.BaseActivity;
import com.example.administrator.myapplication.bussiness.constract.MainConstract;
import com.example.administrator.myapplication.bussiness.persent.MainPresenter;
import com.example.administrator.myapplication.common.TConst;
import com.example.administrator.myapplication.evenbus.BusEvent;
import com.example.administrator.myapplication.evenbus.EventBusId;
import com.example.administrator.myapplication.evenbus.PlayerEvent;
import com.example.administrator.myapplication.model.ADModel;
import com.example.administrator.myapplication.model.GetPlanJson;
import com.example.administrator.myapplication.model.HeartBeatJson;
import com.example.administrator.myapplication.model.HeartbeatInfo;
import com.example.administrator.myapplication.model.PlanInfo;
import com.example.administrator.myapplication.model.PlanforResult;
import com.example.administrator.myapplication.model.PlanlistJson;
import com.example.administrator.myapplication.net.download.DownloadQueueHelper;
import com.example.administrator.myapplication.setting.SettingDialog;
import com.example.administrator.myapplication.utils.L;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;


public class MainActivity extends BaseActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,MainConstract.MainView{
    private final String TAG = "MainActivity";
    MediaPlayer mediaPlayer;
    LinearLayout lin_mode1;
    ImageView iv_pic;
    SurfaceView main_surf1;
    private CameraView cameraView;
    //    private RelativeLayout layout_main;
    int current_play = 0;
    private String cur_ADId="";
    private TextView tv_tips;
    private boolean isSurfaceCreated = false;        //surface是否已经创建好
    private int curIndex = 0;
    private boolean isPlaying = false;
    private long mExitTime = 0;
    private static final int MAX_EXIT_INTERVAL = 2000;
    private SettingDialog settingDialog;
    private List<BaseDownloadTask> mTaskList = new ArrayList<>();
    private byte[] temp;
    private List<ADModel> adModelList;
    private ADModel cur_Ad;
    private MainPresenter mainPresenter;
    private PlanInfo curPlanInfo;
    private String cur_planID;
    private void play(ADModel adModel) {
        stopPlayer();
        if (adModel == null) {
            return;
        }
        cur_Ad=adModel;
        tv_tips.setText("当前播放：" + "广告" + adModel.getID() + "|" + "模板" + adModel.getPlay_type());
        iv_pic.setVisibility(adModel.getPlay_type() == 2 ? View.VISIBLE : View.GONE);
        if (mediaPlayer == null) {
            CreateMediaPlayer();
        }
        if (cur_ADId.equals(adModel.getID())) {
            if (curIndex > 0) {
                mediaPlayer.seekTo(curIndex);
                curIndex = 0;
            }
            mediaPlayer.start();
        } else {
            try {
                open(adModel.getVideo_url());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        switch (adModel.getPlay_type()) {
            case 1:
                break;
            case 2:

                iv_pic.setImageResource(adModel.getImage_url());
                mediaPlayer.setDisplay(main_surf1.getHolder());
                break;
        }
        cur_ADId=adModel.getID();
    }

    private void initPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        CreateMediaPlayer();
        // 把输送给surfaceView的视频画面，直接显示到屏幕上,不要维持它自身的缓冲区
        CreateSurfaceView();
    }

    private void CreateMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    private void CreateSurfaceView() {
//        main_surf2.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
//        BaseDownloadTask task = FileDownloader.getImpl().create(mSong.getMp3FilePath())
//                .setPath(Constant.File.getMusicDir()+ mSong.getMusicFilename());
//        mTaskList.add(task);
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
                if (current_play < adModelList.size() - 1) {
                    current_play++;
                } else {
                    current_play = 0;
                }
                play(adModelList.get(current_play));
                break;
            case EventBusId.heartbeat:
                HeartBeatJson heartBeatJson = new HeartBeatJson();
                if(cur_Ad!=null) {
                    heartBeatJson.setAdID(cur_Ad.getID());
                    heartBeatJson.setPlanID(String.valueOf(cur_Ad.getPlay_type()));
                }
//                heartBeatJson.setMac(DeviceUtil.getMac());
                heartBeatJson.setMac("e558779714542319");
                L.test("heartBeatJson:"+heartBeatJson.toString());
                mainPresenter.fetchHeartbeat(heartBeatJson.toString());
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        L.d(TAG, "onCompletion");
        isPlaying=false;
    }

    /**
     * 页面从前台到后台会执行 onPause ->onStop 此时Surface会被销毁，
     * 再一次从后台 到前台时需要 重新创建Surface
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isSurfaceCreated) {
            CreateSurfaceView();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        play();
//        L.d("mac:"+DeviceUtil.getCupChipID());

//        File file = new File(TConst.getApkDir(), "test");

    }

    private void startDownload() {
        mTaskList.clear();
        File file = new File(TConst.getApkDir(), "test");
        if (!file.exists()) {
            BaseDownloadTask task = FileDownloader.getImpl().create("http://111.230.222.252:8982/file/zy20150801%E5%8C%97%E4%BA%AC.7z")
                    .setPath(TConst.getApkDir()+ "test");
            mTaskList.add(task);
        }
        DownloadQueueHelper.getInstance().downloadSequentially(mTaskList);
        DownloadQueueHelper.getInstance().setOnDownloadListener(new DownloadQueueHelper.OnDownloadListener() {
            @Override
            public void onDownloadComplete(BaseDownloadTask task) {
                L.test("下载完成");
            }

            @Override
            public void onDownloadTaskError(BaseDownloadTask task, Throwable e) {
                L.test("加载文件失败,请重新下载！");
            }

            @Override
            public void onDownloadProgress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task == null) {
                    return;
                }
                L.test((int) ((float) soFarBytes / totalBytes * 100) + "% 文件加载中...");
            }

            @Override
            public void onDownloadTaskOver() {
                L.test("队列内所有文件下载完成");
            }
        });
    }


    private void play() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSurfaceCreated&&adModelList!=null&&adModelList.size()>0)
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
        stopPlayer();
        initPlayer();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        mainPresenter=new MainPresenter(this);
        tv_tips = findViewById(R.id.tv_tips);
        lin_mode1 = findViewById(R.id.lin_mode);
        iv_pic = findViewById(R.id.iv_pic);
        main_surf1 = findViewById(R.id.main_surf);
        cameraView=findViewById(R.id.view_bottom);
//        cameraView.setPreviewResolution(CAMERA_VIEW_WIDTH,CAMERA_VIEW_HEIGHT);
    }

    @Override
    public void setListener() {
        MyApplication application = (MyApplication) getApplication();
        application.init();
        tv_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startDownload();
                showSetting();
            }
        });
        cameraView.setPreviewCallback(new CameraView.PreviewCallback() {
            @Override public void onGetYuvFrame(byte[] data) {
                temp = data;
                Log.e("lin","===lin===>  onGetYuvFrame");
            }
        });
    }

    @Override
    public void initData() {
        initPlan(getDefPlan());
        initPlayer();
//        cameraView.startCamera();
    }

    private PlanInfo getDefPlan() {
        PlanInfo planInfo=new PlanInfo();
        List<ADModel> list_Ad;
        list_Ad = Arrays.asList(
                new ADModel("1", 2, R.drawable.pic01, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio01)),
                new ADModel("2", 2, R.drawable.pic02, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio02)),
                new ADModel("3", 2, R.drawable.pic03, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio03)),
                new ADModel("4", 2, R.drawable.pic04, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio04)),
                new ADModel("5", 2, R.drawable.pic05, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio05)),
                new ADModel("6", 2, R.drawable.pic06, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio06)),
                new ADModel("7", 2, R.drawable.pic07, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio07)),
                new ADModel("8", 2, R.drawable.pic08, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio08)),
                new ADModel("9", 2, R.drawable.pic09, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio09)),
                new ADModel("10", 2, R.drawable.pic10, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vedio10))

        );
        planInfo.setPlanID("0");
        planInfo.setAdModelList(list_Ad);
        planInfo.setSecond(20);
        planInfo.setTotal(4);
        return planInfo;
    }


    private void initPlan(PlanInfo planInfo){
        if(planInfo!=null&&!TextUtils.isEmpty(planInfo.getPlanID())&&planInfo.getAdModelList()!=null&&planInfo.getAdModelList().size()>0) {
            cur_planID = planInfo.getPlanID();
            syncTime = planInfo.getTotal();
            nextTime = planInfo.getSecond();
            adModelList = planInfo.getAdModelList();
        }
        startScreenTimer();
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
            JsonArray jsonArray = parser.parse(responseBody.string()).getAsJsonArray();
            Gson gson = new Gson();
            ArrayList<HeartbeatInfo> heartbeatInfoArrayList = new ArrayList<>();

            //加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                //使用GSON，直接转成Bean对象
                HeartbeatInfo heartbeatInfo = gson.fromJson(user, HeartbeatInfo.class);
                heartbeatInfoArrayList.add(heartbeatInfo);
            }
            String newPlanId=heartbeatInfoArrayList.get(0).getNewPlanID();
            if(!TextUtils.isEmpty(newPlanId)&&!newPlanId.equals(cur_planID)){
                GetPlanJson getPlanJson=new GetPlanJson();
                getPlanJson.setPlanID(newPlanId);
                getPlanJson.setMac("e558779714542319");
                L.test(getPlanJson.toString());
            mainPresenter.fetchPlan(getPlanJson.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OngetPlan(ResponseBody responseBody) {
        try {
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
            int profileID=planforResultArrayList.get(0).getProfileID();
            //    getPlaylist/{"playlistID":"36","total":"30","mac":"e558779714542319"}
            PlanlistJson planlistJson=new PlanlistJson();
            planlistJson.setPlaylistID(String.valueOf(profileID));
            planlistJson.setMac("e558779714542319");
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
            L.test(responseBody.string());
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
            if(settingDialog==null){
                settingDialog=new SettingDialog();
            }
            if(!settingDialog.isAdded()) {
                settingDialog.show(getSupportFragmentManager(), "setting");
            }
        }
    }

}
