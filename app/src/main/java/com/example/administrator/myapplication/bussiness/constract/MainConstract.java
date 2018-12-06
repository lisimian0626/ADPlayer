package com.example.administrator.myapplication.bussiness.constract;


import com.example.administrator.myapplication.base.BasePresenter;
import com.example.administrator.myapplication.base.BaseView;
import com.example.administrator.myapplication.model.HeartbeatInfo;
import com.google.gson.JsonElement;

import org.json.JSONArray;

import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author: Hanson
 * date:   2016/8/31
 * describe:
 */
public interface MainConstract {
    interface MainView extends BaseView {
        void OnHeartbeat(ResponseBody responseBody);
        void OngetPlan(HeartbeatInfo heartbeatInfo);
    }
    interface MainPresenter extends BasePresenter {
        void fetchHeartbeat(String json);
        void fetchPlan(String json);
    }
}
