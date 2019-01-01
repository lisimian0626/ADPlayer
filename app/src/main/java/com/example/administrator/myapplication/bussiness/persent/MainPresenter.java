package com.example.administrator.myapplication.bussiness.persent;


import com.example.administrator.myapplication.base.CommonPresenter;
import com.example.administrator.myapplication.bussiness.constract.MainConstract;
import com.example.administrator.myapplication.exception.AbsExceptionEngine;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

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
                    public void accept(ResponseBody responseBody) throws Exception {
                        mView.OnHeartbeat(responseBody);
                    }
                }, new AbsExceptionEngine() {
                    @Override
                    public void handMessage(String message) {
                        mView.onFeedBack(false, method, message);
                    }
                });

        addDispose(disposable);
    }

    @Override
    public void fetchPlan(String json) {
        final String method = getMethodName();
        Disposable disposable = mApiService.getPlan(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        mView.OngetPlan(responseBody);
                    }
                }, new AbsExceptionEngine() {
                    @Override
                    public void handMessage(String message) {
                        mView.onFeedBack(false, method, message);
                    }
                });

        addDispose(disposable);
    }

    @Override
    public void fetctPlanList(String json) {
        final String method = getMethodName();
        Disposable disposable = mApiService.getPlaylist(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        mView.OnGetPlanList(responseBody);
                    }
                }, new AbsExceptionEngine() {
                    @Override
                    public void handMessage(String message) {
                        mView.onFeedBack(false, method, message);
                    }
                });

        addDispose(disposable);
    }

    @Override
    public void fetchApkInfo(String json) {
        final String method = getMethodName();
        Disposable disposable = mApiService.getApkInfo(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        mView.OnGetApkInfo(responseBody);
                    }
                }, new AbsExceptionEngine() {
                    @Override
                    public void handMessage(String message) {
                        mView.onFeedBack(false, method, message);
                    }
                });

        addDispose(disposable);
    }
}
