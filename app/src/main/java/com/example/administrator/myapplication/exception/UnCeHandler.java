package com.example.administrator.myapplication.exception;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.MyApplication;
import com.example.administrator.myapplication.utils.L;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by J Wong on 2017/5/26.
 */

public class UnCeHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static final String TAG = "UnCeHandler";
    private MyApplication application;

    public UnCeHandler(MyApplication application) {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!handleException(ex) && mDefaultHandler != null){
            mDefaultHandler.uncaughtException(thread, ex);
        }else{
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                L.e(TAG, "error : "+e.toString());
            }
            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
            @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(application.getApplicationContext(), 0, intent,Intent.FLAG_ACTIVITY_NEW_TASK);
            AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis(),restartIntent);
            application.finishActivity();
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(application.getApplicationContext(), "很抱歉,程序出现异常,即将退出.",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
//        saveCrashInfo2File(ex);
        return true;
    }

    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yy-MM-dd HH-mm-ss");

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "  " + timestamp + ".log";
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                File dir = KaraokeSdHelper.getCrash();
//                File file = new File(dir, fileName);
//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(sb.toString().getBytes());
//                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
