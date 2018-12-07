package com.example.administrator.myapplication.bussiness.constract;


import com.example.administrator.myapplication.base.BasePresenter;
import com.example.administrator.myapplication.base.BaseView;
import com.example.administrator.myapplication.model.HeartbeatInfo;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * author: Hanson
 * date:   2016/8/31
 * describe:
 */
public interface MainConstract {
    interface MainView extends BaseView {
        void OnHeartbeat(ResponseBody responseBody);
        void OngetPlan(ResponseBody responseBody);
        void OnGetPlanList(ResponseBody responseBody);
    }
    interface MainPresenter extends BasePresenter {
        void fetchHeartbeat(String json);
        void fetchPlan(String json);
        void fetctPlanList(String json);
    }

}
