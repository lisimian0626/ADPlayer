package com.example.administrator.myapplication.upgrade.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.common.TConst;
import com.example.administrator.myapplication.net.download.DownloadQueueHelper;
import com.example.administrator.myapplication.utils.L;
import com.example.administrator.myapplication.utils.PackageUtil;
import com.example.administrator.myapplication.utils.ToastUtills;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by J Wong on 2016/12/9.
 */

public class DlgProgress extends BaseDialog {

    private TextView tvTitle;
    private TextView tvTip;
    private TextView tvProgress;
    private ProgressBar mProgressBar;
    private List<BaseDownloadTask> mTaskList = new ArrayList<>();
    public DlgProgress(Activity context) {
        super(context, R.style.MyDialog);
        init();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTip(String tip) {
        tvTip.setText(tip);
    }

    public void setProgress(long progress, long total) {
        tvProgress.setText(progress + "/" + total);
        mProgressBar.setProgress((int) ((100 * progress) / total));
    }

    void init() {
        this.setContentView(R.layout.dlg_progress);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = 500;
        lp.height = 300;
        lp.gravity = Gravity.CENTER;

        getWindow().setAttributes(lp);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        mProgressBar = (ProgressBar) findViewById(R.id.pgb_progress);
        setCanceledOnTouchOutside(false);
    }





    public void startDownload(String downloadUrl) {
        FileDownloader.getImpl()
                .create(downloadUrl)
                .setPath(TConst.getApkDir() + "/" + TConst.DOWN_APK_NAME + ".apk")
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        setProgress(soFarBytes,totalBytes);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        super.blockComplete(task);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        setProgress(task.getSmallFileSoFarBytes(),task.getSmallFileTotalBytes());
                        dismiss();
                        PackageUtil.installApkByApi(getContext(), new File(task.getPath()));
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        ToastUtills.showToast(getContext(),"下载错误,等待重试");
                    }
                })
                .start();
    }
}