package com.example.administrator.myapplication.bussiness.persent;


import com.example.administrator.myapplication.base.CommonPresenter;
import com.example.administrator.myapplication.bussiness.constract.MainConstract;
import com.example.administrator.myapplication.exception.AbsExceptionEngine;
import com.example.administrator.myapplication.model.HeartbeatInfo;
import com.google.gson.JsonElement;

import org.json.JSONArray;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author: Hanson
 * date:   2016/9/19
 * describe:
 */
public class MainPresenter extends CommonPresenter<MainConstract.MainView> implements MainConstract.MainPresenter{

    public MainPresenter(MainConstract.MainView view) {
        super(view);
    }

    @Override
    public void fetchHeartbeat(String json) {
        final String method = getMethodName();
        Disposable disposable = mApiService.heartbeat(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody respond) throws Exception {
                     mView.OnHeartbeat(respond);
                    }
                }, new AbsExceptionEngine() {
                    @Override
                    public void handMessage(String message) {
                        mView.onFeedBack(false,method,message);
                    }
                });

        addDispose(disposable);
    }

    @Override
    public void fetchPlan(String json) {

    }
}
