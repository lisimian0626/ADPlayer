package com.example.administrator.myapplication.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装了获取文件路径的一些方法
 */
public final class StorageUtil {
    //获取手机内部可用空间大小 :
    public static long getInternalStorageAvailableSpace()
    {
        long ret=0;
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        ret = availableBlocks * blockSize/1024/1024;   //MB
        L.d("test","ret="+ret);
        return ret;
    }


    //获取手机外部可用空间大小:
    public long getSDCardAvailableSpace()
    {
        long ret=0;
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        ret = availableBlocks * blockSize;
        return ret;
    }
}