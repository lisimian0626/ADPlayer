package com.example.administrator.myapplication.utils;

import android.text.TextUtils;

import com.example.administrator.myapplication.common.TConst;

/**
 * Created by J Wong on 2015/10/20 09:57.
 */
public class ServerFileUtil {

    private final static String USB_PATH = "mnt/usbhost1/";

    private final static String TAG = ServerFileUtil.class.getSimpleName();


    /**
     * 获取新版本文件地址
     * @param filePath
     * @return
     */
    public static String getVersionFileUrl(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }

        return (filePath.startsWith("http://") ||
                filePath.startsWith("udp://")) ? filePath : (TConst.VERSION_UPDATE_URL + filePath);
    }
}
