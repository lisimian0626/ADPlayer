package com.example.administrator.myapplication.setting;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.evenbus.BusEvent;
import com.example.administrator.myapplication.evenbus.EventBusHelper;
import com.example.administrator.myapplication.evenbus.EventBusId;
import com.example.administrator.myapplication.utils.PreferenceUtil;


public  class SettingDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener{
    View mRootView;
    private TextView close;
    private ToggleButton boot,resume,camera;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置style
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SettingDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置 dialog 的宽高
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置 dialog 的背景为 null
        getDialog().setCanceledOnTouchOutside(false);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.setting,container,false);
        close=mRootView.findViewById(R.id.setting_close);
        boot=mRootView.findViewById(R.id.setting_tg_boot);
        resume=mRootView.findViewById(R.id.setting_tg_resume);
        camera=mRootView.findViewById(R.id.setting_tg_camera);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        boot.setChecked(PreferenceUtil.getBoolean(getContext(), "boot", true));
        resume.setChecked(PreferenceUtil.getBoolean(getContext(), "resume", true));
        camera.setChecked(PreferenceUtil.getBoolean(getContext(), "camera", true));
        boot.setOnCheckedChangeListener(this);
        resume.setOnCheckedChangeListener(this);
        camera.setOnCheckedChangeListener(this);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.dimAmount=0.0f;
        lp.gravity = Gravity.CENTER; //底部
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mRootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.setting_tg_boot:
                PreferenceUtil.setBoolean(getContext(), "boot", b);
                break;
            case R.id.setting_tg_resume:
                PreferenceUtil.setBoolean(getContext(), "resume", b);
                break;
            case R.id.setting_tg_camera:
                PreferenceUtil.setBoolean(getContext(), "camera", b);
                if(b){
                    EventBusHelper.sendEvent(BusEvent.getEvent(EventBusId.startCamera));
                }else{
                    EventBusHelper.sendEvent(BusEvent.getEvent(EventBusId.closeCamera));
                }

                break;
//            case R.id.setting_tg_hdmi:
//                PreferenceUtil.setBoolean(getContext(), "hdmi", b);
//                break;
        }
    }
}
