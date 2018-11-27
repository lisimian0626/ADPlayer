package com.example.administrator.myapplication.bussiness.function;


import com.example.administrator.myapplication.bussiness.constract.ServerConstract;
import com.example.administrator.myapplication.net.RetrofitUtils;
import com.example.administrator.myapplication.net.service.ApiService;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * author: Hanson
 * date:   2016/9/19
 * describe:
 */
public class ServerFunction implements ServerConstract.Model {


    @Override
    public Observable<ResponseBody> heartbeat(String json) {
        return RetrofitUtils.newInstence()
                .create(ApiService.class)
                .heartbeat(json);
    }
}
