package com.example.administrator.myapplication.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class SleepService extends Service {
    private PowerManager.WakeLock mWakeLock;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();
        new Thread(new LockScreenRunbale()).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class LockScreenRunbale implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * 30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isOnTime()) {
                    // 触发POWER事件
                    execShell("input keyevent 26");
                }
                if (isOffTime()) {
                    // 触发POWER事件
                    execShell("input keyevent 26");
                }
            }
        }
    }

    public void execShell(String cmd) {
        try {
            // 权限设置
            Process p = Runtime.getRuntime().exec("sh");
            // 获取输出流
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            // 将命令写入
            dataOutputStream.writeBytes(cmd);
            // 提交命令
            dataOutputStream.flush();
            // 关闭流操作
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * 保持电源锁，防止系统进入休眠后关机
     */
    private void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    //释放设备电源锁，目前未使用
    private void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    /**
     * 判断是否已经到关机时间
     *
     * @return
     */
    private boolean isOffTime() {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int minute = c.get(Calendar.MINUTE);

        if (minute % 4 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否已经到开机时间
     *
     * @return
     */
    private boolean isOnTime() {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int minute = c.get(Calendar.MINUTE);

        if (minute % 4 == 2) {
            return true;
        }
        return false;
    }

}
