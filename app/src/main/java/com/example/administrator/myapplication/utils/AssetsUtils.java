package com.example.administrator.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.administrator.myapplication.R;

import java.io.IOException;
import java.io.InputStream;

public class AssetsUtils {
    private static int[] resId = {R.drawable.guangzhoutajiu, R.drawable.haitian, R.drawable.zhengmian};

    public static Bitmap readBitmap(Context context, String fileName) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    public static int getDefaultRes() {
        return resId[DeviceUtil.getNum(3)];
    }

}
