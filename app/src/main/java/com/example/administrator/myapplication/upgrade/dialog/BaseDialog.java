package com.example.administrator.myapplication.upgrade.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by J Wong on 2017/5/22.
 */

public class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Logger.d("BaseDialog", "dispatchTouchEvent ===============");
//        BaseActivity.mLastTouchTime = System.currentTimeMillis();
        return super.dispatchTouchEvent(ev);
    }


}
